#!/bin/bash

# Icon Generator Script for Study Library Manager
# This script converts the SVG icon to various PNG sizes needed for the application

echo "🎨 Generating application icons..."

# Check if rsvg-convert is available (part of librsvg)
if ! command -v rsvg-convert &> /dev/null; then
    echo "⚠️  rsvg-convert not found. Installing..."
    
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        if command -v brew &> /dev/null; then
            brew install librsvg
        else
            echo "❌ Please install Homebrew first: https://brew.sh"
            exit 1
        fi
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        read -p "⚠️  This script needs to install 'librsvg2-bin' using sudo. Continue? [y/N] " confirm
        if [[ "$confirm" =~ ^[Yy]$ ]]; then
            sudo apt-get update && sudo apt-get install -y librsvg2-bin
        else
            echo "❌ Installation cancelled by user."
            exit 1
        fi
    else
        echo "❌ Unsupported OS. Please install librsvg manually."
        exit 1
    fi
fi

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SVG_FILE="$SCRIPT_DIR/app-icon.svg"
OUTPUT_DIR="$SCRIPT_DIR"

# Check if SVG file exists
if [ ! -f "$SVG_FILE" ]; then
    echo "❌ SVG file not found: $SVG_FILE"
    exit 1
fi

# Generate PNG files in various sizes
echo "📐 Generating PNG files..."

# Main application icon (used by JavaFX)
rsvg-convert -w 256 -h 256 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon.png"
echo "✅ Created app-icon.png (256x256)"

# Additional sizes for different platforms
rsvg-convert -w 16 -h 16 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-16.png"
echo "✅ Created app-icon-16.png"

rsvg-convert -w 32 -h 32 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-32.png"
echo "✅ Created app-icon-32.png"

rsvg-convert -w 48 -h 48 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-48.png"
echo "✅ Created app-icon-48.png"

rsvg-convert -w 64 -h 64 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-64.png"
echo "✅ Created app-icon-64.png"

rsvg-convert -w 128 -h 128 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-128.png"
echo "✅ Created app-icon-128.png"

rsvg-convert -w 512 -h 512 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-512.png"
echo "✅ Created app-icon-512.png"

# For macOS .icns (optional)
rsvg-convert -w 1024 -h 1024 "$SVG_FILE" -o "$OUTPUT_DIR/app-icon-1024.png"
echo "✅ Created app-icon-1024.png"

echo ""
echo "🎉 All icons generated successfully!"
echo "📁 Location: $OUTPUT_DIR"
echo ""
echo "Next steps:"
echo "  1. Run 'mvn clean package' to rebuild the application"
echo "  2. The icon will now appear in the application window"
echo ""
