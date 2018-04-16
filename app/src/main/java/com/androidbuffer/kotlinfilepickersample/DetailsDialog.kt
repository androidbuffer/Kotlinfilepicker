package com.androidbuffer.kotlinfilepickersample

import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.androidbuffer.kotlinfilepicker.KotResult

/**
 * Created by AndroidBuffer on 13/4/18.
 */
class DetailsDialog : DialogFragment() {

    private lateinit var nameOfFile: TextView
    private lateinit var sizeOfFile: TextView
    private lateinit var locationOfFile: TextView
    private lateinit var typeOfFile: TextView
    private lateinit var modifiedDateOfFile: TextView

    companion object {

        val EXTRA_RESULT_OF_FILE = "EXTRA_FILE_RESULT"

        fun getInstance(kotResult: KotResult): DetailsDialog {
            val fragment = DetailsDialog()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_RESULT_OF_FILE, kotResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.dialog_details, container, false)
        return view!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assert(arguments != null)
        if (view == null) return
        //view init
        nameOfFile = view.findViewById(R.id.tvNameOfFile)
        sizeOfFile = view.findViewById(R.id.tvSizeOfFile)
        locationOfFile = view.findViewById(R.id.tvLocationOfFile)
        typeOfFile = view.findViewById(R.id.tvTypeOfFile)
        modifiedDateOfFile = view.findViewById(R.id.tvModiedDateOfFile)

        view.findViewById<AppCompatButton>(R.id.btClose).setOnClickListener({ dismiss() })

        val kotResult = arguments?.getParcelable<KotResult>(EXTRA_RESULT_OF_FILE)
        nameOfFile.text = kotResult?.name
        sizeOfFile.text = String.format(getString(R.string.size_of_file, kotResult?.size))
        locationOfFile.text = String.format(getString(R.string.location_of_file, kotResult?.location))
        typeOfFile.text = String.format(getString(R.string.type_of_file, kotResult?.type))
        modifiedDateOfFile.text = String.format(getString(R.string.modified_date, kotResult?.modified))
    }

}
