package ar.com.novaclean

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ar.com.novaclean.Models.User
import ar.com.novaclean.Models._user
import ar.com.novaclean.Utils.DownloadImageTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "empleado"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [fichaEmpleadoChica.OnEmpleadoFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fichaEmpleadoChica.newInstance] factory method to
 * create an instance of this fragment.
 */
class fichaEmpleadoChica : Fragment() {
    // TODO: Rename and change types of parameters
    private var employee: _user? = null

    private var listenerEmpleado: OnEmpleadoFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.getSerializable(ARG_PARAM1) as _user
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_ficha_empleado_chica, container, false)
        val imageView = root.findViewById<ImageView>(R.id.ivFotoEmpleado)
        val textView = root.findViewById<TextView>(R.id.tvNombreEmpleado)
        val cardView = root.findViewById<CardView>(R.id.cvEmpleadoFicha)
        employee?.let{
            DownloadImageTask(imageView)
                    .execute(it.photo_url)
            textView.text=it.name + ", " + it.last_name

        }
        if(employee != null)
        {
            cardView.setOnClickListener(View.OnClickListener { listenerEmpleado?.onFragmentInteraction(employee!!) })
        }

        return root
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEmpleadoFragmentInteractionListener) {
            listenerEmpleado = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnEmpleadoFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerEmpleado = null
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
    interface OnEmpleadoFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(employee: _user)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param employee Parameter 1.
         * @return A new instance of fragment fichaEmpleadoChica.
         */
        @JvmStatic
        fun newInstance(employee: _user) =
                fichaEmpleadoChica().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, employee)
                    }
                }
    }
}
