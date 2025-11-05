package com.documentvault.util;

/**
 * DocumentVault Brand Constants
 * 
 * Central repository for all branding, messaging, and identity constants
 * used throughout the DocumentVault application.
 * 
 * DocumentVault - Your Personal Document Security System
 * Privacy-First • Offline-Only • Bank-Level Security
 */
public final class BrandConstants {

    // ========================================
    // APPLICATION IDENTITY
    // ========================================

    /** Application name */
    public static final String APP_NAME = "DocumentVault";

    /** Application tagline */
    public static final String APP_TAGLINE = "Your Personal Document Security System";

    /** Application version */
    public static final String APP_VERSION = "2.0.0";

    /** Full application title for windows */
    public static final String APP_TITLE = APP_NAME + " - " + APP_TAGLINE;

    /** Application copyright */
    public static final String APP_COPYRIGHT = "Copyright © 2025 DocumentVault";

    /** Application license */
    public static final String APP_LICENSE = "Licensed under MIT License";

    // ========================================
    // PRIVACY & SECURITY MESSAGING
    // ========================================

    /** Core privacy promise */
    public static final String PRIVACY_PROMISE = "Your Documents Stay Yours";

    /** Offline guarantee */
    public static final String OFFLINE_GUARANTEE = "100% Offline - No Cloud Dependencies";

    /** Security level description */
    public static final String SECURITY_LEVEL = "Bank-Level Encryption";

    /** Privacy features summary */
    public static final String PRIVACY_FEATURES = "• 100% Offline - No Cloud Dependencies\n" +
            "• Bank-Level Encryption - AES-256 Security\n" +
            "• Professional Grade - HIPAA Ready\n" +
            "• Zero Tracking - Complete Privacy";

    // ========================================
    // PROFESSIONAL POSITIONING
    // ========================================

    /** Professional readiness statement */
    public static final String PROFESSIONAL_READY = "HIPAA-Ready Document Security";

    /** Target audience description */
    public static final String TARGET_USERS = "For Privacy-Conscious Professionals";

    /** Professional compliance message */
    public static final String COMPLIANCE_MESSAGE = "Professional-Grade Security for Healthcare, Legal & Financial Use";

    // ========================================
    // USER INTERFACE TEXT
    // ========================================

    /** About dialog content */
    public static final String ABOUT_CONTENT = APP_NAME + " v" + APP_VERSION + "\n" +
            APP_TAGLINE + "\n\n" +
            "Privacy-First Document Management\n" +
            PRIVACY_FEATURES + "\n\n" +
            APP_COPYRIGHT + "\n" +
            APP_LICENSE;

    /** Welcome message for new users */
    public static final String WELCOME_MESSAGE = "Welcome to " + APP_NAME + "!\n\n" +
            "Your personal documents are now secured with " +
            "bank-level encryption and complete privacy. " +
            "Everything stays on your device - no cloud, no tracking.";

    /** Menu labels - updated for document security context */
    public static final String MENU_FILE = "File";
    public static final String MENU_EDIT = "Documents"; // Changed from "Edit" to reflect document focus
    public static final String MENU_VIEW = "View";
    public static final String MENU_SECURITY = "Security"; // New menu for security features
    public static final String MENU_HELP = "Help";

    // ========================================
    // DOCUMENT TYPE LABELS
    // ========================================

    /** Document type descriptions for user interface */
    public static final String DOC_TYPE_NOTE = "Secure Note";
    public static final String DOC_TYPE_PDF = "PDF Document";
    public static final String DOC_TYPE_MEDIA = "Media Link";
    public static final String DOC_TYPE_SNIPPET = "Text Snippet";
    public static final String DOC_TYPE_MEDICAL = "Medical Record";
    public static final String DOC_TYPE_CERTIFICATE = "Certificate";
    public static final String DOC_TYPE_FINANCIAL = "Financial Document";
    public static final String DOC_TYPE_LEGAL = "Legal Document";

    // ========================================
    // STATUS MESSAGES
    // ========================================

    /** Status bar messages */
    public static final String STATUS_READY = "DocumentVault Ready - All Data Encrypted & Secure";
    public static final String STATUS_LOADING = "Loading Documents...";
    public static final String STATUS_SAVING = "Saving Document Securely...";
    public static final String STATUS_ENCRYPTED = "Document Encrypted Successfully";

    // ========================================
    // SECURITY CLASSIFICATIONS
    // ========================================

    /** Security level labels */
    public static final String SECURITY_PUBLIC = "Public";
    public static final String SECURITY_SENSITIVE = "Sensitive";
    public static final String SECURITY_RESTRICTED = "Restricted";
    public static final String SECURITY_CONFIDENTIAL = "Confidential";

    // ========================================
    // TOOLTIPS & HELP TEXT
    // ========================================

    /** Enhanced tooltips with security context */
    public static final String TOOLTIP_ADD_DOCUMENT = "Add a new document with automatic encryption";
    public static final String TOOLTIP_SECURITY_LEVEL = "Set security classification for this document";
    public static final String TOOLTIP_ENCRYPTION = "All documents are automatically encrypted with AES-256";
    public static final String TOOLTIP_PRIVACY = "Your data never leaves this device - 100% private";

    // ========================================
    // ERROR & WARNING MESSAGES
    // ========================================

    /** Security-focused error messages */
    public static final String ERROR_ENCRYPTION_FAILED = "Failed to encrypt document - please try again";
    public static final String ERROR_ACCESS_DENIED = "Access denied - insufficient security clearance";
    public static final String WARNING_SENSITIVE_DATA = "This document contains sensitive information";

    // Prevent instantiation
    private BrandConstants() {
    }
}