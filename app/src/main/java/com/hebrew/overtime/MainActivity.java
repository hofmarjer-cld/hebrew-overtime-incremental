package com.hebrew.overtime;

/*
    MainActivity.java - The Heart of Your Android App
    =================================================
    
    🎯 WHAT IS THIS FILE?
    This is your app's main Activity - the first screen users see when they open your app.
    Think of it like the "main()" function in other programming languages, but for Android.
    
    📚 KEY ANDROID CONCEPTS YOU'LL LEARN:
    1. Activity Lifecycle - How Android manages your app's life and death
    2. User Interface - How to show things on screen
    3. Event Handling - How to respond to user interactions
    4. Logging - How to debug your app professionally
    5. Resource Management - How Android handles different screen sizes/languages
    
    🧠 ACTIVITY LIFECYCLE EXPLAINED:
    Android manages memory aggressively. Your app can be:
    - Created: User opens app
    - Started: App becomes visible  
    - Resumed: User can interact with app
    - Paused: Another app comes to foreground (phone call, notification)
    - Stopped: App is no longer visible
    - Destroyed: Android kills app to free memory
    
    📱 WHY IS LIFECYCLE IMPORTANT?
    - Save user's work before app gets killed
    - Resume exactly where user left off
    - Release resources when not needed
    - Handle interruptions gracefully (phone calls, etc.)
*/

// 📦 IMPORTS - The Tools We Need
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

/*
    🔍 IMPORT EXPLANATION:
    - Activity: Base class for all screens in Android
    - Bundle: Container for data passed between Activities
    - Log: Professional debugging tool (like console.log in JavaScript)
    - View: Base class for all UI elements
    - Button: Clickable button widget
    - TextView: Widget that displays text
    - Toast: Small popup message to user
*/

/**
 * 🏠 MainActivity - Your App's Home Screen
 * 
 * 📚 EXTENDS Activity EXPLANATION:
 * By extending Activity, our class inherits all the Android system integration:
 * - Automatic lifecycle method calls
 * - UI rendering capabilities  
 * - System event handling
 * - Memory management
 * 
 * 🎯 THINK OF IT LIKE:
 * Activity is like a "smart frame" that Android provides.
 * You just need to tell it what to display and how to behave.
 */
public class MainActivity extends Activity {
    
    /*
        🏷️ LOG TAG - Your App's Debug Signature
        =======================================
        
        🎯 PURPOSE: Identifies your app's log messages among thousands of system logs
        📱 USAGE: Log.d(TAG, "Your message here")
        🔍 FILTERING: In logcat, filter by "HelloWorldApp" to see only your messages
        
        💡 PROFESSIONAL TIP:
        Always use a consistent TAG across your app for easy debugging.
        Many developers use the class name: private static final String TAG = "MainActivity";
    */
    private static final String TAG = "HebrewOvertimeApp";
    
    /*
        🧩 UI COMPONENT REFERENCES
        ==========================
        
        🎯 WHY PRIVATE FIELDS?
        We store references to UI elements as private fields so we can:
        - Access them from any method in this Activity
        - Modify their properties (text, color, visibility)
        - Set up event listeners (click handlers)
        
        📱 THESE WILL CONNECT TO:
        - welcomeText: TextView showing "Hello World!" message
        - clickMeButton: Button that users can tap
        - clickCountText: TextView showing how many times button was clicked
    */
    private TextView welcomeText;
    private Button clickMeButton;
    private Button datePickerButton;  // v002f: Date picker button
    private EditText hoursInput;      // v003f: Hours input field
    private EditText minutesInput;    // v003f: Minutes input field
    private TextView clickCountText;
    private TextView historyText;     // v005f: History display
    
    /*
        📊 APP STATE VARIABLES - v007f (ISRAELI CALENDAR INTEGRATION)
        =============================================================
        
        🎯 PURPOSE: Track overtime data with Israeli calendar awareness
        📱 OVERTIME TRACKING:
        - selectedDate: Date for overtime entry (YYYY-MM-DD format)
        - clickCount: Number of overtime entries (loaded from SharedPreferences)
        - sharedPreferences: Android's built-in key-value storage for app data
        - israeliHolidays2025: Israeli holidays for accurate work days calculation
        
        💾 DATA PERSISTENCE - v004f:
        SharedPreferences automatically saves data between app sessions.
        
        🇮🇱 ISRAELI CALENDAR - v007f:
        Comprehensive Israeli holiday support for accurate business calculations.
        This ensures work days calculation respects Israeli public holidays.
    */
    private String selectedDate = "2025-08-03";  // Today's date (default)
    private int clickCount = 0;
    private SharedPreferences sharedPreferences;  // v004f: Data persistence
    
    // v007f: Israeli holidays for 2025 (comprehensive list)
    private final java.util.Map<String, String> israeliHolidays2025 = new java.util.HashMap<String, String>() {{
        // Major holidays
        put("2025-01-01", "New Year's Day");
        put("2025-04-13", "Passover Eve");
        put("2025-04-14", "Passover");
        put("2025-04-15", "Passover");
        put("2025-04-20", "Last Day of Passover");
        put("2025-05-01", "Labor Day");
        put("2025-05-14", "Independence Day");
        put("2025-06-02", "Shavuot Eve");
        put("2025-06-03", "Shavuot");
        put("2025-09-15", "Rosh Hashanah Eve");
        put("2025-09-16", "Rosh Hashanah");
        put("2025-09-17", "Rosh Hashanah");
        put("2025-09-24", "Yom Kippur Eve");
        put("2025-09-25", "Yom Kippur");
        put("2025-09-29", "Sukkot Eve");
        put("2025-09-30", "Sukkot");
        put("2025-10-06", "Simchat Torah");
        // Fast days (typically work days but some companies observe)
        put("2025-07-27", "Fast of Tammuz");
        put("2025-08-14", "Tisha B'Av");
    }};
    
    /*
        🏗️ onCreate() - THE BIRTH OF YOUR ACTIVITY
        ==========================================
        
        📚 WHAT IS onCreate()?
        This method is called when Android creates your Activity for the first time.
        It's like a constructor, but specifically for Android components.
        
        🎯 WHAT HAPPENS IN onCreate():
        1. Android creates Activity object in memory
        2. Calls onCreate() to let you initialize your app
        3. You set up UI, variables, event listeners, etc.
        4. After onCreate() completes, Activity becomes visible
        
        📱 REAL-WORLD ANALOGY:
        onCreate() is like setting up a new office:
        - Arrange furniture (set up UI layout)
        - Connect phone and internet (set up event listeners)
        - Put nameplate on door (set title, icon)
        - Organize desk supplies (initialize variables)
        
        🔄 LIFECYCLE TIMING:
        onCreate() → onStart() → onResume() → User can interact with app
        
        ⚠️ IMPORTANT RULES:
        1. Always call super.onCreate(savedInstanceState) first
        2. Always call setContentView() to show your UI
        3. Initialize variables and UI components here
        4. Don't do heavy work here - it blocks the UI thread
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🚨 CRITICAL: Always call parent's onCreate first
        // This lets Android do its system-level initialization
        super.onCreate(savedInstanceState);
        
        // 📱 PROFESSIONAL LOGGING: Track when Activity is created
        Log.d(TAG, "🏗️ onCreate() called - Activity is being created!");
        
        // 💾 v004f: Initialize SharedPreferences for data persistence
        sharedPreferences = getSharedPreferences("HebrewOvertimeData", MODE_PRIVATE);
        
        // Load saved click count from previous sessions
        clickCount = sharedPreferences.getInt("clickCount", 0);
        Log.d(TAG, "📊 Loaded click count from storage: " + clickCount);
        
        /*
            🎨 CREATE UI PROGRAMMATICALLY
            =============================
            
            Since we want to maintain GitHub Actions compatibility,
            we'll create the UI in code instead of using XML layouts.
            This eliminates resource file dependencies.
        */
        
        // Create main welcome text
        welcomeText = new TextView(this);
        welcomeText.setText("🕐 מעקב שעות נוספות 🕐\nHebrew Overtime Tracker\nBuilt with GitHub Actions CI/CD");
        welcomeText.setTextSize(20);
        welcomeText.setPadding(50, 50, 50, 30);
        welcomeText.setGravity(android.view.Gravity.CENTER);
        
        // Create date picker button - v002f
        datePickerButton = new Button(this);
        datePickerButton.setText("בחר תאריך: " + selectedDate);
        datePickerButton.setTextSize(16);
        datePickerButton.setPadding(40, 15, 40, 15);
        
        // Create hours input field - v003f
        hoursInput = new EditText(this);
        hoursInput.setHint("שעות (0-23)");
        hoursInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        hoursInput.setTextSize(16);
        hoursInput.setPadding(30, 15, 30, 15);
        hoursInput.setGravity(android.view.Gravity.CENTER);
        
        // Create minutes input field - v003f
        minutesInput = new EditText(this);
        minutesInput.setHint("דקות (0-59)");
        minutesInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        minutesInput.setTextSize(16);
        minutesInput.setPadding(30, 15, 30, 15);
        minutesInput.setGravity(android.view.Gravity.CENTER);
        
        // Create overtime add button
        clickMeButton = new Button(this);
        clickMeButton.setText("הוסף שעות נוספות");
        clickMeButton.setTextSize(18);
        clickMeButton.setPadding(40, 20, 40, 20);
        
        // Create overtime display
        clickCountText = new TextView(this);
        clickCountText.setTextSize(16);
        clickCountText.setPadding(50, 20, 50, 20);
        clickCountText.setGravity(android.view.Gravity.CENTER);
        
        // Create history display - v005f
        historyText = new TextView(this);
        historyText.setTextSize(14);
        historyText.setPadding(50, 10, 50, 50);
        historyText.setGravity(android.view.Gravity.CENTER);
        historyText.setBackgroundColor(0xFFF5F5F5); // Light gray background
        
        // Set initial click count display
        updateClickCountDisplay();
        
        // Create a vertical layout programmatically
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setGravity(android.view.Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);
        
        // Add views to layout - v005f
        layout.addView(welcomeText);
        layout.addView(datePickerButton);  // Date picker first
        layout.addView(hoursInput);        // Hours input
        layout.addView(minutesInput);      // Minutes input
        layout.addView(clickMeButton);     // Then add overtime button
        layout.addView(clickCountText);    // Overview statistics
        layout.addView(historyText);       // History for selected date
        
        // Set the layout as our content view
        setContentView(layout);
        
        Log.d(TAG, "🎨 UI created programmatically");
        Log.d(TAG, "🔗 UI components initialized");
        
        /*
            👆 EVENT HANDLING - RESPONDING TO USER INTERACTIONS
            ==================================================
            
            🎯 WHAT IS setOnClickListener()?
            This method tells Android: "When user taps this button, run this code"
            
            📱 EVENT-DRIVEN PROGRAMMING CONCEPT:
            Your app doesn't run continuously. Instead, it sleeps until something happens:
            - User clicks button → onClick() runs
            - Phone rotates → onConfigurationChanged() runs  
            - App loses focus → onPause() runs
            - User types text → onTextChanged() runs
            
            🧠 ANONYMOUS INNER CLASS EXPLAINED:
            new View.OnClickListener() { ... } creates a small object whose only job
            is to handle button clicks. It's like hiring a specialist just for this task.
            
            💡 MODERN ALTERNATIVE - Lambda Expressions:
            Modern Java allows: clickMeButton.setOnClickListener(v -> handleButtonClick());
            But explicit OnClickListener is clearer for learning.
        */
        
        // Date picker button click listener - v002f
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        
        // Overtime add button click listener
        clickMeButton.setOnClickListener(new View.OnClickListener() {
            /*
                🎯 onClick() - BUTTON CLICK HANDLER
                ==================================
                
                This method runs every time user taps the button.
                The 'v' parameter is the View (button) that was clicked.
            */
            @Override
            public void onClick(View v) {
                // 📊 Track button clicks for educational purposes
                Log.d(TAG, "🖱️ Button clicked! Processing click #" + (clickCount + 1));
                
                // Call our custom method to handle the click logic
                handleButtonClick();
            }
        });
        
        Log.d(TAG, "👆 Button click listener set up successfully");
        Log.d(TAG, "✅ onCreate() completed - Activity ready for user interaction!");
    }
    
    /*
        🎯 CUSTOM METHOD - HANDLE BUTTON CLICK LOGIC
        ============================================
        
        🧠 WHY SEPARATE METHOD?
        Instead of putting all logic in onClick(), we create a separate method:
        - Easier to read and understand
        - Can be called from other places if needed
        - Easier to test
        - Follows "Single Responsibility Principle"
        
        📱 WHAT THIS METHOD DOES:
        1. Increment click counter
        2. Update UI to show new count
        3. Show encouraging message to user
        4. Log the event for debugging
    */
    private void handleButtonClick() {
        // v003f: Get hours and minutes from input fields
        try {
            String hoursStr = hoursInput.getText().toString().trim();
            String minutesStr = minutesInput.getText().toString().trim();
            
            // Default to 0 if empty
            int hours = hoursStr.isEmpty() ? 0 : Integer.parseInt(hoursStr);
            int minutes = minutesStr.isEmpty() ? 0 : Integer.parseInt(minutesStr);
            
            // Validate ranges (following original overtime tracker)
            if (hours < 0 || hours > 23) {
                Toast.makeText(this, "שעות חייבות להיות בין 0 ל-23", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (minutes < 0 || minutes > 59) {
                Toast.makeText(this, "דקות חייבות להיות בין 0 ל-59", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (hours == 0 && minutes == 0) {
                Toast.makeText(this, "אנא הזן לפחות דקה אחת", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Add entry and increment counter
            clickCount++;
            
            // v004f: Save overtime entry to SharedPreferences
            saveOvertimeEntry(selectedDate, hours, minutes);
            
            Log.d(TAG, "🕐 Added overtime: " + hours + "h " + minutes + "m (entry #" + clickCount + ")");
            
            // Clear input fields after successful entry
            hoursInput.setText("");
            minutesInput.setText("");
            
            // Update display and show feedback
            updateClickCountDisplay();
            showHebrewOvertimeMessage(hours, minutes);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "אנא הזן מספרים תקינים בלבד", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Number format error in input fields", e);
        }
    }
    
    /*
        📅 DATE PICKER METHOD - v002f
        =============================
        
        🎯 PURPOSE: Show standard Android date picker dialog
        
        🧠 HOW IT WORKS:
        - Gets current calendar instance
        - Creates DatePickerDialog with Hebrew context
        - Updates selectedDate when user picks date
        - Updates button text and display
    */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Format as YYYY-MM-DD (month is 0-based, so add 1)
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    
                    // Update button text with new date
                    datePickerButton.setText("בחר תאריך: " + selectedDate);
                    
                    // Update overtime display
                    updateClickCountDisplay();
                    
                    Log.d(TAG, "📅 Date selected: " + selectedDate);
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
        Log.d(TAG, "📅 Date picker dialog shown");
    }
    
    /*
        🔄 UI UPDATE METHOD - KEEP DISPLAY IN SYNC
        ==========================================
        
        🎯 PURPOSE: Update the click count display whenever count changes
        
        🧠 WHY SEPARATE METHOD?
        - Called from multiple places (onCreate, handleButtonClick)
        - Keeps UI update logic in one place
        - Easy to modify display format later
        - Follows DRY (Don't Repeat Yourself) principle
    */
    private void updateClickCountDisplay() {
        // v006f: Display monthly statistics and summary calculations
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2025, 7, 3); // August 2025 (month is 0-based)
        
        // Calculate monthly statistics
        int workDays = calculateWorkDaysInMonth(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH));
        float requiredHours = workDays * 1.0f; // 1 hour per work day
        float actualHours = calculateActualHoursForMonth(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH));
        float balance = actualHours - requiredHours;
        float completionPercentage = requiredHours > 0 ? (actualHours / requiredHours) * 100 : 0;
        
        // v007f: Get holiday information for this month
        int holidaysInMonth = countHolidaysInMonth(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH));
        
        // Format monthly summary in Hebrew with Israeli calendar awareness
        String monthName = getHebrewMonthName(cal.get(java.util.Calendar.MONTH));
        StringBuilder summaryBuilder = new StringBuilder();
        summaryBuilder.append("📊 ").append(monthName).append(" ").append(cal.get(java.util.Calendar.YEAR)).append("\n");
        summaryBuilder.append("ימי עבודה: ").append(workDays);
        if (holidaysInMonth > 0) {
            summaryBuilder.append(" (חגים: ").append(holidaysInMonth).append(")");
        }
        summaryBuilder.append("\n");
        summaryBuilder.append("שעות נדרשות: ").append(String.format("%.1f", requiredHours)).append("\n");
        summaryBuilder.append("שעות בפועל: ").append(String.format("%.1f", actualHours)).append("\n");
        summaryBuilder.append("יתרה: ").append(String.format("%.1f", balance));
        if (balance >= 0) {
            summaryBuilder.append(" ✅");
        } else {
            summaryBuilder.append(" ❌");
        }
        summaryBuilder.append("\nהשלמה: ").append(String.format("%.0f%%", completionPercentage));
        
        // v007f: Show if selected date is a holiday
        if (isIsraeliHoliday(selectedDate)) {
            summaryBuilder.append("\n🎄 ").append(selectedDate).append(": ").append(getHolidayName(selectedDate));
        }
        
        clickCountText.setText(summaryBuilder.toString());
        
        // v005f: Update history display for selected date
        updateHistoryDisplay();
        
        Log.d(TAG, "🔄 Monthly summary updated: " + workDays + " work days, " + actualHours + "h actual, " + balance + "h balance");
    }
    
    /*
        🎉 USER FEEDBACK METHOD - MAKE THE APP FEEL ALIVE
        ================================================
        
        🎯 PURPOSE: Show different encouraging messages based on click count
        
        📱 TOAST EXPLANATION:
        Toast is a small popup message that appears briefly and disappears automatically.
        Perfect for showing quick feedback without interrupting user flow.
        
        🧠 UX (USER EXPERIENCE) PRINCIPLE:
        Good apps provide immediate feedback for user actions. Even a simple
        button click should feel responsive and engaging.
    */
    private void showHebrewOvertimeMessage(int hours, int minutes) {
        // v003f: Show message with actual hours and minutes entered
        String timeStr = "";
        if (hours > 0 && minutes > 0) {
            timeStr = hours + " שעות ו-" + minutes + " דקות";
        } else if (hours > 0) {
            timeStr = hours + " שעות";
        } else if (minutes > 0) {
            timeStr = minutes + " דקות";
        }
        
        String message;
        if (clickCount == 1) {
            message = "🎉 מעולה! הוספת " + timeStr + "!";
        } else if (clickCount <= 5) {
            message = "👍 כל הכבוד! הוספת " + timeStr + " (רשומה #" + clickCount + ")";
        } else {
            message = "🏆 מדהים! " + timeStr + " נוספו (רשומה #" + clickCount + ")";
        }
        
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        
        Log.d(TAG, "💬 Showed Hebrew overtime message: " + message);
    }
    
    /*
        💾 DATA PERSISTENCE METHOD - v004f
        ==================================
        
        🎯 PURPOSE: Save overtime entry to SharedPreferences
        
        🧠 HOW IT WORKS:
        - Creates unique key for each date-entry combination
        - Stores hours, minutes, and timestamp
        - Updates total click count
        - All data persists between app sessions
        
        📱 DATA FORMAT:
        - Key: "entry_YYYY-MM-DD_N" (N = entry number for that date)
        - Value: "hours:minutes:timestamp"
        - Counter: "clickCount" = total entries
    */
    private void saveOvertimeEntry(String date, int hours, int minutes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        // Create unique key for this entry
        String entryKey = "entry_" + date + "_" + clickCount;
        
        // Create entry data (hours:minutes:timestamp)
        String entryData = hours + ":" + minutes + ":" + System.currentTimeMillis();
        
        // Save entry data
        editor.putString(entryKey, entryData);
        
        // Update total click count
        editor.putInt("clickCount", clickCount);
        
        // Commit changes to storage
        editor.apply();
        
        Log.d(TAG, "💾 Saved overtime entry: " + entryKey + " = " + entryData);
    }
    
    /*
        📊 HISTORY DISPLAY METHOD - v005f
        =================================
        
        🎯 PURPOSE: Show overtime entries for the selected date
        
        🧠 HOW IT WORKS:
        - Searches SharedPreferences for entries matching selected date
        - Formats entries as readable Hebrew text
        - Shows hours, minutes, and entry time
        - Provides visual feedback that data persistence is working
        
        📱 DISPLAY FORMAT:
        "תאריך: YYYY-MM-DD
         רשומה 1: Xh Ym
         רשומה 2: Xh Ym
         (אם אין רשומות - הודעה מתאימה)"
    */
    private void updateHistoryDisplay() {
        // Get all SharedPreferences keys
        java.util.Map<String, ?> allEntries = sharedPreferences.getAll();
        java.util.List<String> dateEntries = new java.util.ArrayList<>();
        
        // Find entries for selected date
        for (java.util.Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("entry_" + selectedDate + "_")) {
                String value = (String) entry.getValue();
                dateEntries.add(value);
            }
        }
        
        // Build history display text
        StringBuilder historyBuilder = new StringBuilder();
        historyBuilder.append("📅 תאריך: ").append(selectedDate).append("\n");
        
        if (dateEntries.isEmpty()) {
            historyBuilder.append("אין רשומות לתאריך זה");
        } else {
            historyBuilder.append("רשומות לתאריך:\n");
            for (int i = 0; i < dateEntries.size(); i++) {
                String entryData = dateEntries.get(i);
                String[] parts = entryData.split(":");
                if (parts.length >= 2) {
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    historyBuilder.append("• ");
                    if (hours > 0 && minutes > 0) {
                        historyBuilder.append(hours).append(" שעות ו-").append(minutes).append(" דקות");
                    } else if (hours > 0) {
                        historyBuilder.append(hours).append(" שעות");
                    } else if (minutes > 0) {
                        historyBuilder.append(minutes).append(" דקות");
                    }
                    historyBuilder.append("\n");
                }
            }
        }
        
        historyText.setText(historyBuilder.toString());
        Log.d(TAG, "📊 Updated history display for " + selectedDate + " - " + dateEntries.size() + " entries");
    }
    
    /*
        📊 MONTHLY CALCULATIONS METHODS - v006f
        =======================================
        
        🎯 PURPOSE: Core business logic for overtime tracking
        
        These methods implement the key calculations that make this a real
        overtime tracker, following the original overtime tracker logic:
        - Work days calculation (excluding weekends)
        - Required hours (work days × 1 hour)
        - Actual hours (sum of saved entries)
        - Balance and completion percentage
    */
    
    private int calculateWorkDaysInMonth(int year, int month) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        
        int workDays = 0;
        int holidaysExcluded = 0;
        
        for (int day = 1; day <= daysInMonth; day++) {
            cal.set(year, month, day);
            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
            
            // Check if it's a potential work day (Monday-Thursday)
            if (dayOfWeek >= java.util.Calendar.MONDAY && dayOfWeek <= java.util.Calendar.THURSDAY) {
                // v007f: Check if it's an Israeli holiday
                String dateStr = String.format("%04d-%02d-%02d", year, month + 1, day);
                
                if (israeliHolidays2025.containsKey(dateStr)) {
                    // It's a holiday - don't count as work day
                    holidaysExcluded++;
                    Log.d(TAG, "🎄 Holiday excluded: " + dateStr + " - " + israeliHolidays2025.get(dateStr));
                } else {
                    // Regular work day
                    workDays++;
                }
            }
        }
        
        Log.d(TAG, "📅 Work days for " + year + "/" + (month+1) + ": " + workDays + " (excluded " + holidaysExcluded + " holidays)");
        return workDays;
    }
    
    private float calculateActualHoursForMonth(int year, int month) {
        // Get all saved entries for this month
        java.util.Map<String, ?> allEntries = sharedPreferences.getAll();
        float totalHours = 0.0f;
        
        String monthPrefix = String.format("%04d-%02d", year, month + 1);
        
        for (java.util.Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("entry_") && key.contains(monthPrefix)) {
                String value = (String) entry.getValue();
                String[] parts = value.split(":");
                if (parts.length >= 2) {
                    try {
                        int hours = Integer.parseInt(parts[0]);
                        int minutes = Integer.parseInt(parts[1]);
                        totalHours += hours + (minutes / 60.0f);
                    } catch (NumberFormatException e) {
                        Log.w(TAG, "Failed to parse entry: " + key);
                    }
                }
            }
        }
        
        Log.d(TAG, "💰 Calculated actual hours for " + year + "/" + (month+1) + ": " + totalHours);
        return totalHours;
    }
    
    private String getHebrewMonthName(int month) {
        String[] hebrewMonths = {
            "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני",
            "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"
        };
        return hebrewMonths[month];
    }
    
    /*
        🇮🇱 ISRAELI HOLIDAY HELPER METHODS - v007f
        ===========================================
        
        🎯 PURPOSE: Israeli calendar integration utilities
        
        These methods provide Israeli holiday detection and related
        functionality for accurate work days calculation.
    */
    
    private boolean isIsraeliHoliday(String dateStr) {
        return israeliHolidays2025.containsKey(dateStr);
    }
    
    private String getHolidayName(String dateStr) {
        return israeliHolidays2025.get(dateStr);
    }
    
    private int countHolidaysInMonth(int year, int month) {
        int holidays = 0;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        
        for (int day = 1; day <= daysInMonth; day++) {
            String dateStr = String.format("%04d-%02d-%02d", year, month + 1, day);
            if (isIsraeliHoliday(dateStr)) {
                holidays++;
            }
        }
        
        return holidays;
    }
    
    /*
        🔄 ACTIVITY LIFECYCLE METHODS - UNDERSTANDING YOUR APP'S LIFE
        =============================================================
        
        🧠 WHY OVERRIDE THESE METHODS?
        Android can pause, stop, or destroy your Activity at any time to manage memory.
        By overriding these methods, you can:
        - Save user's work before losing focus
        - Release resources when not needed
        - Resume exactly where user left off
        - Handle device rotation gracefully
        
        📱 LIFECYCLE FLOW:
        onCreate() → onStart() → onResume() → [USER INTERACTION] → 
        onPause() → onStop() → onDestroy()
    */
    
    /*
        🚀 onStart() - ACTIVITY BECOMING VISIBLE
        ========================================
        
        📚 WHEN IS THIS CALLED?
        - After onCreate() when Activity is first created
        - When returning from another Activity
        - When user returns to app from background
        
        🎯 WHAT TO DO HERE:
        - Start animations
        - Register broadcast receivers  
        - Begin location updates
        - Start timers or background tasks
    */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "🚀 onStart() called - Activity is becoming visible to user");
    }
    
    /*
        ▶️ onResume() - ACTIVITY READY FOR INTERACTION
        ==============================================
        
        📚 WHEN IS THIS CALLED?  
        - After onStart() when Activity is first created
        - When returning from pause (phone call ended, notification dismissed)
        - When user switches back to your app
        
        🎯 WHAT TO DO HERE:
        - Resume animations or video playback
        - Resume camera preview
        - Start intensive CPU operations
        - Register sensors (accelerometer, GPS)
        
        💡 THIS IS WHERE USERS CAN INTERACT:
        Only when onResume() completes can users actually touch and interact with your app.
    */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "▶️ onResume() called - Activity ready for user interaction");
        Log.d(TAG, "📊 Current state: clickCount = " + clickCount);
    }
    
    /*
        ⏸️ onPause() - ACTIVITY LOSING FOCUS
        ====================================
        
        📚 WHEN IS THIS CALLED?
        - Another Activity comes to foreground (phone call, notification)
        - User opens another app
        - Screen turns off
        - User opens recent apps menu
        
        🎯 WHAT TO DO HERE:
        - Pause video/audio playback
        - Save user's current work (draft email, game progress)
        - Stop animations
        - Release camera resources
        
        ⚠️ IMPORTANT: This method should complete quickly!
        The new Activity can't show until your onPause() finishes.
    */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "⏸️ onPause() called - Activity losing focus");
        Log.d(TAG, "💾 Would save user data here in a real app");
    }
    
    /*
        ⏹️ onStop() - ACTIVITY NO LONGER VISIBLE
        ========================================
        
        📚 WHEN IS THIS CALLED?
        - User navigates to another Activity in your app
        - User switches to completely different app
        - User goes to home screen
        
        🎯 WHAT TO DO HERE:
        - Stop network requests
        - Unregister broadcast receivers
        - Release expensive resources
        - Stop background tasks
        
        💾 DATA PERSISTENCE:
        This is your last guaranteed chance to save important data.
        Android might kill your app after this without calling onDestroy().
    */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "⏹️ onStop() called - Activity no longer visible");
        Log.d(TAG, "🔌 Would stop network requests and background tasks here");
    }
    
    /*
        🔄 onRestart() - ACTIVITY COMING BACK TO LIFE
        =============================================
        
        📚 WHEN IS THIS CALLED?
        - When user returns to your Activity after it was stopped
        - Between onStop() and onStart()
        
        🎯 WHAT TO DO HERE:
        - Refresh data that might have changed while stopped
        - Restart services that were stopped
        - Update UI with latest information
    */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "🔄 onRestart() called - Activity restarting after being stopped");
    }
    
    /*
        💀 onDestroy() - ACTIVITY'S FINAL MOMENT
        ========================================
        
        📚 WHEN IS THIS CALLED?
        - User finishes Activity (presses back button)
        - System needs memory and kills your Activity
        - Device configuration changes (rotation)
        
        🎯 WHAT TO DO HERE:
        - Release ALL remaining resources
        - Unregister any remaining listeners
        - Clean up to prevent memory leaks
        - Final data save if not done already
        
        ⚠️ WARNING: This method might NOT be called!
        If system is low on memory, it might kill your app without calling onDestroy().
        Always save important data in onPause() or onStop().
    */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "💀 onDestroy() called - Activity is being destroyed");
        Log.d(TAG, "🧹 Final cleanup - releasing all resources");
        Log.d(TAG, "📊 Final click count was: " + clickCount);
    }
    
    /*
        📋 LIFECYCLE SUMMARY FOR BEGINNERS
        ==================================
        
        🔄 NORMAL FLOW:
        onCreate() → onStart() → onResume() → [User interacts] → 
        onPause() → onStop() → onDestroy()
        
        🔄 INTERRUPTED FLOW (Phone call, notification):
        onResume() → onPause() → [Interruption] → onResume()
        
        🔄 BACKGROUND/FOREGROUND:
        onResume() → onPause() → onStop() → [App in background] → 
        onRestart() → onStart() → onResume()
        
        💡 KEY PRINCIPLES:
        1. Save data in onPause() - it's the last guaranteed call
        2. Release resources in onStop() - app might stay stopped
        3. Don't do heavy work in onCreate() - it blocks UI
        4. Always call super.method() first in lifecycle methods
        
        🎯 PROFESSIONAL DEBUGGING:
        Watch the logcat output when using your app to see these methods
        being called. This understanding is crucial for Android development!
    */
}