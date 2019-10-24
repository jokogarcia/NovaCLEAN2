package ar.com.novaclean

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ar.com.novaclean.Models.VisitEvent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_EVENTO = "evento"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [evento_list_fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [evento_list_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class evento_list_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var visitEvent: VisitEvent
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            visitEvent = it.getSerializable(ARG_EVENTO) as VisitEvent

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_evento_list_fragment, container, false)
        val TV1 = root.findViewById<TextView>(R.id.tvEvTitulo)
        val TV2 = root.findViewById<TextView>(R.id.tvEvHora)
        val CV = root.findViewById<CardView>(R.id.cvEvBody)

        visitEvent?.let{
            TV1.text = "Visita de limpieza"
            TV2.text=it.starts_at.toString()
        }
        CV.setOnClickListener(View.OnClickListener {
            listener?.onSelectedEvent(visitEvent)
        })
        return root

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnEmpleadoFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
         fun onSelectedEvent(visitEvent: VisitEvent?)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment evento_list_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: VisitEvent) =
                evento_list_fragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_EVENTO, param1)
                    }
                }
    }
}
