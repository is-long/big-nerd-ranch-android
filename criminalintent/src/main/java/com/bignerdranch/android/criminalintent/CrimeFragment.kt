package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"

class CrimeFragment: Fragment() {

    // store state
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    // data from DB
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    // public, because they've got to be called by any calling activity host
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID

        crimeDetailViewModel.loadCrime(crimeId)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            // publish and update
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            }
        )
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            // skip checking box animation
            jumpDrawablesToCurrentState()
        }
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

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}