package com.documentvault.controller;

import com.documentvault.model.*;
import com.documentvault.util.AlertUtil;
import com.documentvault.viewmodel.LibraryViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller for the item form (add/edit dialog).
 * Handles creating and editing library items.
 */
public class ItemFormController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField tagsField;

    // Note-specific fields
    @FXML
    private TextArea contentArea;
    @FXML
    private CheckBox markdownCheckBox;

    // PDF-specific fields
    @FXML
    private TextField filePathField;
    @FXML
    private Button browseButton;
    @FXML
    private TextField authorField;
    @FXML
    private TextField pageCountField;

    // Media-specific fields
    @FXML
    private TextField urlField;
    @FXML
    private ComboBox<MediaLink.MediaType> mediaTypeComboBox;
    @FXML
    private TextField sourceField;
    @FXML
    private TextField durationField;

    // Snippet-specific fields
    @FXML
    private TextArea snippetContentArea;
    @FXML
    private TextField languageField;
    @FXML
    private TextField sourceUrlField;

    private LibraryItem item;
    private LibraryViewModel viewModel;
    private boolean isNewItem = false;

    @FXML
    public void initialize() {
        // Setup will be done after setItem is called
    }

    /**
     * Sets the item to edit or create.
     */
    public void setItem(LibraryItem item) {
        this.item = item;
        setupForm();
        populateForm();
    }

    /**
     * Sets the view model for data operations.
     */
    public void setViewModel(LibraryViewModel viewModel) {
        this.viewModel = viewModel;
        categoryComboBox.setItems(viewModel.getCategories());

        // Configure the ComboBox to display category names
        categoryComboBox.setConverter(new javafx.util.StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "";
            }

            @Override
            public Category fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }

                // Check if category already exists
                for (Category category : viewModel.getCategories()) {
                    if (category.getName().equalsIgnoreCase(string.trim())) {
                        return category;
                    }
                }

                // Create new category if it doesn't exist
                Category newCategory = new Category(string.trim());
                viewModel.addCategory(newCategory);
                return newCategory;
            }
        });
    }

    /**
     * Sets whether this is a new item.
     */
    public void setNewItem(boolean isNew) {
        this.isNewItem = isNew;
    }

    /**
     * Sets up the form based on item type.
     */
    private void setupForm() {
        // Show/hide fields based on item type
        switch (item.getItemType()) {
            case NOTE -> {
                showNoteFields();
                hideOtherFields();
            }
            case PDF -> {
                showPdfFields();
                hideOtherFields();
            }
            case MEDIA_LINK -> {
                showMediaFields();
                hideOtherFields();
                mediaTypeComboBox.getItems().setAll(MediaLink.MediaType.values());
            }
            case TEXT_SNIPPET -> {
                showSnippetFields();
                hideOtherFields();
            }
        }
    }

    /**
     * Populates the form with item data.
     */
    private void populateForm() {
        // Common fields
        titleField.setText(item.getTitle());
        descriptionArea.setText(item.getDescription());
        categoryComboBox.setValue(item.getCategory());
        tagsField.setText(String.join(", ", item.getTags()));

        // Type-specific fields
        switch (item.getItemType()) {
            case NOTE -> {
                Note note = (Note) item;
                contentArea.setText(note.getContent());
                markdownCheckBox.setSelected(note.isMarkdown());
            }
            case PDF -> {
                PdfDocument pdf = (PdfDocument) item;
                filePathField.setText(pdf.getFilePath());
                authorField.setText(pdf.getAuthor());
                pageCountField.setText(String.valueOf(pdf.getPageCount()));
            }
            case MEDIA_LINK -> {
                MediaLink media = (MediaLink) item;
                urlField.setText(media.getUrl());
                mediaTypeComboBox.setValue(media.getMediaType());
                sourceField.setText(media.getSource());
                durationField.setText(String.valueOf(media.getDurationMinutes()));
            }
            case TEXT_SNIPPET -> {
                TextSnippet snippet = (TextSnippet) item;
                snippetContentArea.setText(snippet.getContent());
                languageField.setText(snippet.getLanguage());
                sourceUrlField.setText(snippet.getSourceUrl());
            }
        }
    }

    /**
     * Saves the item with form data.
     */
    @FXML
    private void handleSave() {
        if (!validateForm()) {
            return;
        }

        // Update common fields
        item.setTitle(titleField.getText());
        item.setDescription(descriptionArea.getText());

        // Handle category - get value from editable ComboBox
        Category selectedCategory = categoryComboBox.getValue();
        if (selectedCategory == null && categoryComboBox.getEditor().getText() != null
                && !categoryComboBox.getEditor().getText().trim().isEmpty()) {
            // User typed a new category name
            String categoryName = categoryComboBox.getEditor().getText().trim();
            selectedCategory = findOrCreateCategory(categoryName);
        }
        item.setCategory(selectedCategory);

        // Parse and set tags
        String tagsText = tagsField.getText();
        if (tagsText != null && !tagsText.trim().isEmpty()) {
            String[] tags = tagsText.split(",");
            item.getTags().clear();
            for (String tag : tags) {
                item.addTag(tag.trim());
            }
        }

        // Update type-specific fields
        switch (item.getItemType()) {
            case NOTE -> {
                Note note = (Note) item;
                note.setContent(contentArea.getText());
                note.setMarkdown(markdownCheckBox.isSelected());
            }
            case PDF -> {
                PdfDocument pdf = (PdfDocument) item;
                pdf.setFilePath(filePathField.getText());
                pdf.setAuthor(authorField.getText());
                try {
                    pdf.setPageCount(Integer.parseInt(pageCountField.getText()));
                } catch (NumberFormatException e) {
                    pdf.setPageCount(0);
                }
            }
            case MEDIA_LINK -> {
                MediaLink media = (MediaLink) item;
                media.setUrl(urlField.getText());
                media.setMediaType(mediaTypeComboBox.getValue());
                media.setSource(sourceField.getText());
                try {
                    media.setDurationMinutes(Integer.parseInt(durationField.getText()));
                } catch (NumberFormatException e) {
                    media.setDurationMinutes(0);
                }
            }
            case TEXT_SNIPPET -> {
                TextSnippet snippet = (TextSnippet) item;
                snippet.setContent(snippetContentArea.getText());
                snippet.setLanguage(languageField.getText());
                snippet.setSourceUrl(sourceUrlField.getText());
            }
        }

        // Save to view model
        if (isNewItem) {
            viewModel.addItem(item);
        } else {
            viewModel.updateItem(item);
        }

        closeDialog();
    }

    /**
     * Validates the form before saving.
     */
    private boolean validateForm() {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Validation Error", "Title Required",
                    "Please enter a title for this item.");
            return false;
        }

        // Type-specific validation
        switch (item.getItemType()) {
            case PDF -> {
                if (filePathField.getText() == null || filePathField.getText().trim().isEmpty()) {
                    AlertUtil.showWarning("Validation Error", "File Path Required",
                            "Please select a PDF file.");
                    return false;
                }
            }
            case MEDIA_LINK -> {
                if (urlField.getText() == null || urlField.getText().trim().isEmpty()) {
                    AlertUtil.showWarning("Validation Error", "URL Required",
                            "Please enter a URL.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Handles the browse button for PDF selection.
     */
    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showOpenDialog(browseButton.getScene().getWindow());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            if (titleField.getText() == null || titleField.getText().isEmpty()) {
                titleField.setText(file.getName());
            }

            // Update file size if this is a PDF
            if (item instanceof PdfDocument) {
                ((PdfDocument) item).setFileSize(file.length());
            }
        }
    }

    /**
     * Handles the cancel button.
     */
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    /**
     * Closes the dialog.
     */
    private void closeDialog() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    /**
     * Finds an existing category by name or creates a new one.
     */
    private Category findOrCreateCategory(String categoryName) {
        // Check if category already exists (case-insensitive)
        for (Category category : viewModel.getCategories()) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }

        // Create new category with default color
        Category newCategory = new Category(categoryName);
        viewModel.addCategory(newCategory);
        return newCategory;
    }

    // Helper methods to show/hide fields

    private void showNoteFields() {
        if (contentArea != null)
            contentArea.setVisible(true);
        if (contentArea != null)
            contentArea.setManaged(true);
        if (markdownCheckBox != null)
            markdownCheckBox.setVisible(true);
        if (markdownCheckBox != null)
            markdownCheckBox.setManaged(true);
    }

    private void showPdfFields() {
        if (filePathField != null)
            filePathField.setVisible(true);
        if (filePathField != null)
            filePathField.setManaged(true);
        if (browseButton != null)
            browseButton.setVisible(true);
        if (browseButton != null)
            browseButton.setManaged(true);
        if (authorField != null)
            authorField.setVisible(true);
        if (authorField != null)
            authorField.setManaged(true);
        if (pageCountField != null)
            pageCountField.setVisible(true);
        if (pageCountField != null)
            pageCountField.setManaged(true);
    }

    private void showMediaFields() {
        if (urlField != null)
            urlField.setVisible(true);
        if (urlField != null)
            urlField.setManaged(true);
        if (mediaTypeComboBox != null)
            mediaTypeComboBox.setVisible(true);
        if (mediaTypeComboBox != null)
            mediaTypeComboBox.setManaged(true);
        if (sourceField != null)
            sourceField.setVisible(true);
        if (sourceField != null)
            sourceField.setManaged(true);
        if (durationField != null)
            durationField.setVisible(true);
        if (durationField != null)
            durationField.setManaged(true);
    }

    private void showSnippetFields() {
        if (snippetContentArea != null)
            snippetContentArea.setVisible(true);
        if (snippetContentArea != null)
            snippetContentArea.setManaged(true);
        if (languageField != null)
            languageField.setVisible(true);
        if (languageField != null)
            languageField.setManaged(true);
        if (sourceUrlField != null)
            sourceUrlField.setVisible(true);
        if (sourceUrlField != null)
            sourceUrlField.setManaged(true);
    }

    private void hideOtherFields() {
        // This would hide fields not relevant to the current item type
        // Implementation depends on FXML structure
    }
}
