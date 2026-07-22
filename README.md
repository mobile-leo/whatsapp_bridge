# WhatsBridge

WhatsBridge is a lightweight Android application that acts as a bridge between phone numbers and WhatsApp conversations.

When another application sends a phone number using a `tel:` intent, Android may offer **WhatsBridge** as an option in the **"Open with"** dialog (depending on the Android version and the originating app). Once selected, WhatsBridge extracts the phone number, formats it if necessary, opens the corresponding WhatsApp conversation, and immediately closes itself.

## Features

* 📱 Receives `tel:` intents from other Android applications.
* 💬 Opens a WhatsApp conversation without requiring the contact to be saved.
* ⚡ Lightweight and fast.
* 🧹 Automatically sanitizes phone numbers.
* 🚫 No third-party dependencies.

## How It Works

1. Tap a phone number in a compatible Android application.
2. Choose **WhatsBridge** from the **Open with** dialog.
3. WhatsBridge processes the phone number.
4. A WhatsApp conversation is opened automatically.
5. The application closes itself.

> **Note:** Whether the **Open with** dialog is displayed depends on how the originating application launches phone numbers and on Android's intent resolution behavior. Some apps may open the system dialer directly, bypassing the chooser.

## Requirements

* Android 8.0 (API 26) or later
* WhatsApp installed on the device (recommended)

## Dependencies

This project intentionally uses **no external packages**.

Only the Flutter SDK and the Android framework are used.

## Project Structure

```text
lib/
└── main.dart

android/
└── AndroidManifest.xml
```

## License

This project is licensed under the MIT License.
