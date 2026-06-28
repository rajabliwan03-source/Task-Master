package com.example.taskmaster

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class Taskcreationscreen : AppCompatActivity() {

    private lateinit var taskNameLayout: TextInputLayout
    private lateinit var taskNameEditText: TextInputEditText
    private lateinit var taskDescEditText: TextInputEditText
    private lateinit var categoryLayout: TextInputLayout
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var selectedDateText: TextView
    private lateinit var selectedTimeText: TextView
    private lateinit var saveButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    
    private var selectedDateStr: String = ""
    private var selectedTimeStr: String = ""

    private val viewModel: TaskViewModel by viewModels()
    private val firestoreRepository = FirestoreRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_taskcreationscreen)

        initViews()
        setupToolbar()
        setupCategoryDropdown()
        setupPickers()
        setupListeners()
    }

    private fun initViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        taskNameLayout = findViewById(R.id.task_name_layout)
        taskNameEditText = findViewById(R.id.task_name_edit_text)
        taskDescEditText = findViewById(R.id.task_desc_edit_text)
        categoryLayout = findViewById(R.id.category_layout)
        categoryDropdown = findViewById(R.id.category_dropdown)
        selectedDateText = findViewById(R.id.selected_date_text)
        selectedTimeText = findViewById(R.id.selected_time_text)
        saveButton = findViewById(R.id.save_button)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupCategoryDropdown() {
        val categories = arrayOf("Work", "Personal", "Health", "Education", "Shopping", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        categoryDropdown.setAdapter(adapter)
    }

    private fun setupPickers() {
        findViewById<MaterialButton>(R.id.date_picker_button).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Task Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                selectedDateStr = sdf.format(selection)
                selectedDateText.text = getString(R.string.date_format, selectedDateStr)
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        findViewById<MaterialButton>(R.id.time_picker_button).setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Task Time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val amPm = if (timePicker.hour < 12) "AM" else "PM"
                val hour = if ((timePicker.hour % 12) == 0) 12 else timePicker.hour % 12
                selectedTimeStr = String.format(Locale.getDefault(), "%02d:%02d %s", hour, timePicker.minute, amPm)
                selectedTimeText.text = getString(R.string.time_format, selectedTimeStr)
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER")
        }
    }

    private fun setupListeners() {
        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.suggestion_chip_group)
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as com.google.android.material.chip.Chip
            chip.setOnClickListener {
                taskNameEditText.setText(chip.text)
                taskNameEditText.setSelection(taskNameEditText.text?.length ?: 0)
            }
        }

        taskNameEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    taskNameLayout.error = null
                }

                override fun afterTextChanged(s: Editable?) {}
            },
        )

        categoryDropdown.setOnItemClickListener { _, _, _, _ ->
            categoryLayout.error = null
        }

        saveButton.setOnClickListener {
            validateAndSaveTask()
        }
    }

    private fun validateAndSaveTask() {
        val name = taskNameEditText.text.toString().trim()
        val category = categoryDropdown.text.toString().trim()
        val desc = taskDescEditText.text.toString().trim()

        if (name.isEmpty()) {
            taskNameLayout.error = getString(R.string.task_name_required)
            return
        }

        if (category.isEmpty()) {
            categoryLayout.error = getString(R.string.category_required)
            return
        }
        
        if (selectedDateStr.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            return
        }

        // UX: Show loading state
        setLoading(isLoading = true)

        // Upload to Firestore using Coroutines for optimal performance
        lifecycleScope.launch {
            val result = firestoreRepository.saveTask(
                title = name,
                description = desc,
                priority = category, // mapping category to priority for this example
            )

            setLoading(isLoading = false)

            result.onSuccess { docId ->
                // Also save locally to Room for offline support
                viewModel.addTask(name, category, selectedTimeStr, selectedDateStr, desc)
                
                Toast.makeText(this@Taskcreationscreen, "Task uploaded to Firestore! ID: $docId", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { e ->
                Toast.makeText(this@Taskcreationscreen, "Firestore upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        saveButton.isEnabled = !isLoading
        saveButton.text = if (isLoading) "" else getString(R.string.save_task_button)
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
