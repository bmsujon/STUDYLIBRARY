# Application Icons

This directory contains the application icons for Study Library Manager.

## Files

- **app-icon.svg** - Vector source file (512x512)
- **app-icon.png** - Main application icon (256x256) - Used by JavaFX
- **app-icon-\*.png** - Various sizes for different platforms

## Generating Icons

### Automatic (Recommended)

Run the generation script:

```bash
chmod +x generate-icons.sh
./generate-icons.sh
```

This will automatically:

1. Check for required tools (installs if needed)
2. Generate all PNG sizes from SVG
3. Place them in this directory

### Manual (Alternative)

If you prefer manual conversion, you can use:

#### Option 1: Using ImageMagick

```bash
convert -background none -resize 256x256 app-icon.svg app-icon.png
```

#### Option 2: Using Inkscape

```bash
inkscape --export-type="png" --export-width=256 --export-height=256 \
         --export-filename=app-icon.png app-icon.svg
```

#### Option 3: Using rsvg-convert (librsvg)

```bash
rsvg-convert -w 256 -h 256 app-icon.svg -o app-icon.png
```

#### Option 4: Online Converters

- https://cloudconvert.com/svg-to-png
- https://convertio.co/svg-png/

## Icon Sizes

| Size      | File              | Use Case                 |
| --------- | ----------------- | ------------------------ |
| 16x16     | app-icon-16.png   | Windows taskbar (small)  |
| 32x32     | app-icon-32.png   | Windows taskbar (medium) |
| 48x48     | app-icon-48.png   | Windows taskbar (large)  |
| 64x64     | app-icon-64.png   | macOS Retina displays    |
| 128x128   | app-icon-128.png  | macOS dock               |
| 256x256   | app-icon.png      | **Main JavaFX icon**     |
| 512x512   | app-icon-512.png  | High-res displays        |
| 1024x1024 | app-icon-1024.png | macOS .icns generation   |

## Design

The icon features:

- 📚 Open book symbolizing study/library
- 📄 PDF icon on left page
- ▶️ Media play icon on right page
- 🎨 Professional blue gradient background
- 📏 Clean, modern design suitable for all platforms

## Installing Required Tools

### macOS

```bash
brew install librsvg
```

### Ubuntu/Debian

```bash
sudo apt-get install librsvg2-bin
```

### Windows

Download from: https://sourceforge.net/projects/tumagcc/

Or use WSL with Ubuntu instructions above.

## After Generating

1. Rebuild the application:

   ```bash
   mvn clean package
   ```

2. The icon will automatically appear in:
   - Application window title bar
   - macOS dock
   - Windows taskbar
   - Alt+Tab switcher

## Customization

To create a custom icon:

1. Edit `app-icon.svg` in any vector editor (Inkscape, Adobe Illustrator, Figma)
2. Re-run the generation script
3. Rebuild the application

## Troubleshooting

**Icon not showing?**

- Verify `app-icon.png` exists in this directory
- Check file permissions: `chmod 644 app-icon.png`
- Rebuild: `mvn clean package`
- Clear JavaFX cache: Delete `~/.java/.userPrefs`

**Blurry icon?**

- Generate higher resolution PNG (512x512 or 1024x1024)
- Ensure SVG has no scaling issues

**Script fails?**

- Install librsvg (see instructions above)
- Check SVG file is valid: Open in browser
- Try manual conversion method

## License

The icon design is part of Study Library Manager and follows the same MIT license.
