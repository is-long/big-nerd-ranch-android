package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class CrimeFragment: Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    // public, because they've got to be called by any calling activity host
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        // no inflating fragment's view here; we do it in onCreateView()
    }

    // inflate fragment layout view, and return the View to hosting activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,           // container is the view's parent
        savedInstanceState: Bundle?
    ): View? {
        // explicit inflate,
        // attachToRoot = false -> don't immediately add the inflated view to view's parent
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        // after view inflated, find element in the view
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox =  view.findViewById(R.id.crime_solved) as CheckBox

        dateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        return view
    }

    // set listener on start, instead of on create
    // view state is restored after onCreateView and before onStart()
    //
    // if EditText was set in onCreateView or onCreate
    //    the overridden functions (beforeTextChanged, etc.) will execute
    // so we set listeners in onStart to avoid this behavior,
    // since the listener is hook up AFTER the view state is restored
    override fun onStart() {
        super.onStart()

        // create anon class to implement TextWatcher interface
        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // pass
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // set object prop
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // pass
            }
        }

        titleField.addTextChangedListener(titleWatcher)


        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }
    }
}