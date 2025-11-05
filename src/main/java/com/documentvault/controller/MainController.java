package com.documentvault.controller;

import com.documentvault.model.*;
import com.documentvault.util.AlertUtil;
import com.documentvault.util.DateUtil;
import com.documentvault.util.FileUtil;
import com.documentvault.viewmodel.LibraryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Main controller for the DocumentVault application.
 * 
 * DocumentVault - Your Personal Document Security System
 * Privacy-First • Offline-Only • Bank-Level Security
 * 
 * Manages the main view and coordinates user interactions for secure
 * personal document management. Handles document display, editing,
 * and security operations while maintaining complete privacy.
 */
public class MainController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private ComboBox<LibraryItem.ItemType> typeComboBox;
    @FXML
    private TableView<LibraryItem> itemsTableView;
    @FXML
    private TableColumn<LibraryItem, String> typeColumn;
    @FXML
    private TableColumn<LibraryItem, String> titleColumn;
    @FXML
    private TableColumn<LibraryItem, String> categoryColumn;
    @FXML
    private TableColumn<LibraryItem, LocalDateTime> dateAddedColumn;
    @FXML
    private TableColumn<LibraryItem, LocalDateTime> lastModifiedColumn;
    @FXML
    private TableColumn<LibraryItem, Void> actionsColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private Label itemCountLabel;
    @FXML
    private Button addNoteButton;
    @FXML
    private Button addPdfButton;
    @FXML
    private Button addMediaButton;
    @FXML
    private Button addSnippetButton;

    private LibraryViewModel viewModel;

    @FXML
    public void initialize() {
        viewModel = new LibraryViewModel();
        setupTableColumns();
        setupBindings();
        setupFilters();
        setupTableInteractions();
        updateStatusBar();
    }

    /**
     * Sets up the table columns with appropriate cell factories.
     */
    private void setupTableColumns() {
        // Type column
        typeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getItemType().getDisplayName()));

        // Title column
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Category column
        categoryColumn.setCellValueFactory(data -> {
            Category cat = data.getValue().getCategory();
            return new javafx.beans.property.SimpleStringProperty(
                    cat != null ? cat.getName() : "Uncategorized");
        });

        // Date added column
        dateAddedColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        dateAddedColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : DateUtil.formatDateTime(item));
            }
        });

        // Last modified column
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        lastModifiedColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : DateUtil.getRelativeTime(item));
            }
        });

        // Actions column
        setupActionsColumn();
    }

    /**
     * Sets up the actions column with buttons for each row.
     */
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<LibraryItem, Void> call(TableColumn<LibraryItem, Void> param) {
                return new TableCell<>() {
                    private final Button openButton = new Button("Open");
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox buttonBox = new HBox(5, openButton, editButton, deleteButton);

                    {
                        openButton.getStyleClass().add("action-button");
                        editButton.getStyleClass().add("action-button");
                        deleteButton.getStyleClass().add("action-button-danger");

                        openButton.setOnAction(e -> {
                            LibraryItem item = getTableView().getItems().get(getIndex());
                            handleOpenItem(item);
                        });

                        editButton.setOnAction(e -> {
                            LibraryItem item = getTableView().getItems().get(getIndex());
                            handleEditItem(item);
                        });

                        deleteButton.setOnAction(e -> {
                            LibraryItem item = getTableView().getItems().get(getIndex());
                            handleDeleteItem(item);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttonBox);
                    }
                };
            }
        });
    }

    /**
     * Sets up data bindings with the view model.
     */
    private void setupBindings() {
        itemsTableView.setItems(viewModel.getItems());
        searchField.textProperty().bindBidirectional(viewModel.searchQueryProperty());
    }

    /**
     * Sets up filter controls.
     */
    private void setupFilters() {
        // Category filter
        categoryComboBox.setItems(viewModel.getCategories());
        categoryComboBox.setPromptText("All Categories");
        categoryComboBox.valueProperty().bindBidirectional(viewModel.selectedCategoryProperty());

        // Type filter
        typeComboBox.getItems().addAll(LibraryItem.ItemType.values());
        typeComboBox.setPromptText("All Types");
        typeComboBox.valueProperty().bindBidirectional(viewModel.selectedItemTypeProperty());
    }

    /**
     * Sets up table interactions (double-click, keyboard shortcuts).
     */
    private void setupTableInteractions() {
        // Double-click to edit
        itemsTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LibraryItem selected = itemsTableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handleEditItem(selected);
                }
            }
        });

        // Keyboard shortcuts
        itemsTableView.setOnKeyPressed(event -> {
            LibraryItem selected = itemsTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (event.getCode() == KeyCode.DELETE) {
                    handleDeleteItem(selected);
                } else if (event.getCode() == KeyCode.ENTER) {
                    handleOpenItem(selected);
                }
            }
        });
    }

    /**
     * Handles opening an item based on its type.
     */
    private void handleOpenItem(LibraryItem item) {
        switch (item.getItemType()) {
            case PDF -> {
                PdfDocument pdf = (PdfDocument) item;
                if (!FileUtil.openFile(pdf.getFilePath())) {
                    AlertUtil.showError("Error", "Cannot open PDF",
                            "The file does not exist or cannot be opened.");
                }
            }
            case MEDIA_LINK -> {
                MediaLink media = (MediaLink) item;
                if (!FileUtil.openUrl(media.getUrl())) {
                    AlertUtil.showError("Error", "Cannot open URL",
                            "Failed to open the URL in browser.");
                }
            }
            case TEXT_SNIPPET -> {
                TextSnippet snippet = (TextSnippet) item;
                copyToClipboard(snippet.getContent());
                AlertUtil.showInfo("Copied", "Content Copied",
                        "The snippet has been copied to clipboard.");
            }
            default -> handleEditItem(item);
        }
    }

    /**
     * Handles editing an item.
     */
    private void handleEditItem(LibraryItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemForm.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.setItem(item);
            controller.setViewModel(viewModel);

            Stage stage = new Stage();
            stage.setTitle("Edit " + item.getItemType().getDisplayName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatusBar();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Error", "Cannot open edit form", e.getMessage());
        }
    }

    /**
     * Handles deleting an item.
     */
    private void handleDeleteItem(LibraryItem item) {
        if (AlertUtil.confirmDelete(item.getTitle())) {
            viewModel.deleteItem(item.getId());
            updateStatusBar();
            AlertUtil.showInfo("Deleted", "Item Deleted",
                    item.getTitle() + " has been deleted.");
        }
    }

    /**
     * Copies text to clipboard.
     */
    private void copyToClipboard(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Updates the status bar with current information.
     */
    private void updateStatusBar() {
        int totalItems = viewModel.getTotalItemCount();
        itemCountLabel.setText(totalItems + " item" + (totalItems != 1 ? "s" : ""));
        statusLabel.setText("Last saved: " + DateUtil.formatTime(LocalDateTime.now()));
    }

    // Menu action handlers

    @FXML
    private void handleAddNote() {
        openItemForm(new Note());
    }

    @FXML
    private void handleAddPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showOpenDialog(addPdfButton.getScene().getWindow());
        if (file != null) {
            PdfDocument pdf = new PdfDocument();
            pdf.setFilePath(file.getAbsolutePath());
            pdf.setTitle(file.getName());
            pdf.setFileSize(file.length());
            openItemForm(pdf);
        }
    }

    @FXML
    private void handleAddMedia() {
        openItemForm(new MediaLink());
    }

    @FXML
    private void handleAddSnippet() {
        openItemForm(new TextSnippet());
    }

    /**
     * Opens the item form for creating a new item.
     */
    private void openItemForm(LibraryItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemForm.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.setItem(item);
            controller.setViewModel(viewModel);
            controller.setNewItem(true);

            Stage stage = new Stage();
            stage.setTitle("New " + item.getItemType().getDisplayName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatusBar();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Error", "Cannot open form", e.getMessage());
        }
    }

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        categoryComboBox.setValue(null);
        typeComboBox.setValue(null);
    }

    @FXML
    private void handleRefresh() {
        viewModel.refreshItems();
        viewModel.refreshCategories();
        updateStatusBar();
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) itemsTableView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAbout() {
        AlertUtil.showInfo(
                "About Study Library Manager",
                "Study Library Manager v1.0.0",
                "A personal study library management application.\n\n" +
                        "Developed with JavaFX and Java 17.\n" +
                        "© 2025 All rights reserved.");
    }
}
