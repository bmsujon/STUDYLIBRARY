# Study Library Manager - User Guide

## Table of Contents

1. [Getting Started](#getting-started)
2. [Adding Items](#adding-items)
3. [Managing Items](#managing-items)
4. [Search and Filter](#search-and-filter)
5. [Categories and Tags](#categories-and-tags)
6. [Keyboard Shortcuts](#keyboard-shortcuts)
7. [Data Storage](#data-storage)

## Getting Started

### Installation & Running

1. **Prerequisites**

   - Java 17 or higher installed
   - Maven 3.6+ installed

2. **Build the project**

   ```bash
   cd StudyLibrary
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### First Launch

On first launch, the application will:

- Create a `.studylibrary` folder in your home directory
- Initialize empty JSON files for storing your data
- Display an empty library

## Adding Items

### Adding a Note

1. Click the **"+ Note"** button in the toolbar or use `Ctrl+N`
2. Fill in the details:
   - **Title**: Give your note a descriptive title (required)
   - **Description**: Add a brief description (optional)
   - **Category**: Select or create a category (optional)
   - **Tags**: Add comma-separated tags (e.g., "java, programming, study")
   - **Content**: Write your note content
   - **Markdown**: Check this box if using Markdown formatting
3. Click **Save**

**Use cases:**

- Lecture notes
- Study summaries
- Research notes
- Meeting minutes

### Adding a PDF

1. Click the **"+ PDF"** button or use `Ctrl+P`
2. Click **Browse** to select your PDF file
3. Fill in additional details:
   - **Title**: Auto-filled from filename, can be edited
   - **Author**: Document author
   - **Page Count**: Number of pages
   - **Category & Tags**: For organization
4. Click **Save**

**Use cases:**

- Textbooks
- Research papers
- Course materials
- Reference documents

### Adding a Media Link (Audio/Video)

1. Click the **"+ Media"** button or use `Ctrl+M`
2. Enter the URL (must start with http:// or https://)
3. Fill in details:
   - **Title**: Name of the video/audio
   - **Media Type**: Video, Audio, Podcast, Lecture, or Other
   - **Source**: Platform name (e.g., YouTube, Coursera)
   - **Duration**: Length in minutes
4. Click **Save**

**Use cases:**

- Online lectures
- Tutorial videos
- Podcasts
- Educational YouTube channels

### Adding a Text Snippet

1. Click the **"+ Snippet"** button or use `Ctrl+S`
2. Enter your code or text snippet
3. Specify:
   - **Title**: Short descriptive name
   - **Language**: Programming language or format (java, python, sql, etc.)
   - **Source URL**: Where you found the snippet (optional)
4. Click **Save**

**Use cases:**

- Code snippets
- Useful commands
- SQL queries
- Configuration templates

## Managing Items

### Opening Items

**Double-click** any item or select it and press **Enter** to open:

- **PDFs**: Opens in your system's default PDF viewer
- **Media Links**: Opens in your default web browser
- **Text Snippets**: Copies content to clipboard
- **Notes**: Opens in edit mode

### Editing Items

1. Select an item in the table
2. Click the **Edit** button or double-click the item
3. Make your changes
4. Click **Save**

Changes are automatically saved to disk.

### Deleting Items

1. Select an item
2. Click the **Delete** button or press the **Delete** key
3. Confirm the deletion in the dialog

**Note:** This action cannot be undone!

## Search and Filter

### Search Bar

The search bar at the top right searches across:

- Item titles
- Descriptions
- Content (for notes and snippets)
- Tags
- Categories
- Authors (for PDFs)
- Sources (for media)

Simply type your search query and the table updates in real-time.

### Category Filter

Use the category dropdown to view only items from a specific category.

### Type Filter

Use the type dropdown to filter by item type:

- Notes
- PDF Documents
- Media Links
- Text Snippets

### Combining Filters

You can combine search text with category and type filters for precise results.

**Example:** Search for "java" with type filter set to "Text Snippet" to find all Java code snippets.

### Clearing Filters

Click **Clear Filters** button or use `Ctrl+L` to reset all filters.

## Categories and Tags

### Categories

Categories are single-classification labels for items. Each item can have one category.

**Managing Categories:**

- Categories can be created when adding/editing items
- Future version will include a dedicated category manager

**Use cases:**

- Course names (CS101, Math200)
- Topics (Algorithms, Databases)
- Projects (Project A, Thesis)

### Tags

Tags are flexible, multi-value labels. Each item can have multiple tags.

**Best practices:**

- Use lowercase
- Be consistent
- Keep tags short
- Use commas to separate multiple tags

**Example tags:**

- `important`, `review`, `exam`
- `chapter1`, `chapter2`
- `quick-reference`, `cheatsheet`

## Keyboard Shortcuts

### Global Shortcuts

| Shortcut | Action             |
| -------- | ------------------ |
| `Ctrl+N` | Add new note       |
| `Ctrl+P` | Add new PDF        |
| `Ctrl+M` | Add new media link |
| `Ctrl+S` | Add new snippet    |
| `Ctrl+L` | Clear all filters  |
| `F5`     | Refresh view       |
| `Ctrl+Q` | Exit application   |

### Table Shortcuts

| Shortcut       | Action               |
| -------------- | -------------------- |
| `Enter`        | Open selected item   |
| `Delete`       | Delete selected item |
| `Double-click` | Edit selected item   |

## Data Storage

### Storage Location

All data is stored in JSON format:

- **macOS/Linux**: `~/.studylibrary/`
- **Windows**: `%USERPROFILE%\.studylibrary\`

### Files

- `library-items.json` - All your library items
- `categories.json` - Category definitions

### Backup

**Important:** Regularly backup your `.studylibrary` folder!

You can:

1. Copy the folder to cloud storage (Dropbox, Google Drive)
2. Use version control (git)
3. Create manual backups periodically

### Auto-Save

The application automatically saves:

- When you add a new item
- When you edit an item
- When you delete an item
- When the application closes

### Viewing Raw Data

You can view and edit the JSON files directly if needed:

1. Navigate to the storage directory
2. Open `library-items.json` or `categories.json` in a text editor
3. Make changes carefully (invalid JSON will cause errors)
4. Restart the application to see changes

### Migrating Data

To move your library to another computer:

1. Copy the entire `.studylibrary` folder
2. Paste it in the same location on the new computer
3. Launch the application

## Tips and Best Practices

### Organization

1. **Use consistent naming**: Develop a naming convention for titles
2. **Tag everything**: Tags make searching much easier
3. **Categorize wisely**: Don't create too many categories
4. **Add descriptions**: Brief descriptions help when searching

### For Students

- Create categories for each course
- Use tags for chapter numbers and topics
- Add lecture videos and PDFs immediately after class
- Store snippets of important formulas and code

### For Researchers

- Organize papers by topic or research area
- Tag papers with keywords from abstracts
- Store notes about each paper
- Link to online resources and datasets

### For Developers

- Store useful code snippets with language tags
- Save links to tutorials and documentation
- Keep project-related notes organized
- Tag items by technology stack

## Troubleshooting

### Application Won't Start

- Verify Java 17+ is installed: `java -version`
- Check that JavaFX is properly configured
- Look for error messages in the console

### Data Not Saving

- Check file permissions in `.studylibrary` folder
- Verify disk space is available
- Check for JSON syntax errors in data files

### PDF Won't Open

- Verify the file still exists at the stored path
- Check you have a PDF viewer installed
- Try opening the PDF directly from file system

### URL Won't Open

- Check your internet connection
- Verify the URL is correct
- Ensure you have a default browser set

## Advanced Usage

### Custom Styling

You can modify the appearance by editing:
`src/main/resources/css/application.css`

### Extending Features

The codebase is modular and well-documented:

- Add new item types by extending `LibraryItem`
- Customize the UI by modifying FXML files
- Add new filters in `MainController`

### Integration

The JSON storage format makes it easy to:

- Import data from other applications
- Export to other formats
- Create backup scripts
- Integrate with note-taking apps

## Support and Feedback

For issues or feature requests:

1. Check this guide first
2. Review the README.md file
3. Check the source code documentation
4. Create an issue in your version control system

## Version History

**v1.0.0** - Initial Release

- Four item types (Notes, PDFs, Media, Snippets)
- Full CRUD operations
- Search and filter functionality
- Category and tag system
- Auto-save feature
- Clean, modern UI
