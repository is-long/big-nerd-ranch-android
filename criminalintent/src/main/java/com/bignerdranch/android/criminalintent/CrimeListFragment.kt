package com.bignerdranch.android.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    // delegate on-click events from CrimeListFragment back to hosting activity
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    // called when fragment attached to activity
    // context = hosting activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // unchecked cast, i.e. hosting activity must implement CLF.Callbacks
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    // RecyclerView: ViewGroup(), displays list of child View objects, i.e. item views
    // Each item view rep single object (e.g. LinearLayout) from list of data
    private lateinit var crimeRecyclerView: RecyclerView
    // populate adapter with empty, since db query takes time
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView

        // RecyclerView requires LayoutManager to work
        // Layout manager positions list items on the screen, defines how scrolling work
        // Linear LM position items on the list vertically
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter


        return view
    }

    // called after onCreateView, i.e. after view hierarchy created
    // put observation to make sure the view is ready to display data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            // register observer on LD instance, tie observation life to activity/component
            // this fragment is the scope of the Observer's lifetime

            // LifecycleOwner = implements LCO interface + contains a Lifecycle
            // Lifecycle = obj keeping track of Android's lifecycle's state
            // Fragment is a LCO

            // use fragment's view LC, rather than the Fragment's LC
            // bcs we want to pass data only when the VIEW is in good state (not just the Fragment)
            viewLifecycleOwner,

            // but if observer never cancelled, and tries to update nonexistent view -> crash
            Observer {
                crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            }
        )
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }


    // Each item in RecyclerView must be wrapped in an instance of ViewHolder,
    // which stores ref to item's view
    // view = each item view, passed to ViewHolder constructor
    // viewHolder.itemView now exists
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener {

        // bindable crime
        private lateinit var crime: Crime

        // get ref to itemView's object
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            // set holder as the receiver of the click events
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()

            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {


        // 1
        // once enough viewHolders been created, RV stops calling this method
        // and instead recycle old viewHolders
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            // create a view to display
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)


            //  wrap in ViewHolder
            return CrimeHolder(view)
        }

        // 2, holder from above
        // must be small and efficient to prevent scroll lag
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            // populate holder with crime at position

            // bind data to holder's View
            holder.bind(crime)
        }


        override fun getItemCount(): Int {
            return crimes.size
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }


}