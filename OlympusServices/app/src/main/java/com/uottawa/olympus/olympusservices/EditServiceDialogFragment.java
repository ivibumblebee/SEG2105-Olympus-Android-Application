package com.uottawa.olympus.olympusservices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

public class EditServiceDialogFragment extends DialogFragment{

public interface NoticeDialogListener {
    public void onDialogEdit(DialogFragment dialog);
    public void onDialogDelete(DialogFragment dialog);
    }
    EditServiceDialogFragment.NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EditServiceDialogFragment.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_service_edit, null))
                // Add action buttons
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle args = new Bundle();
                        //@anshu: get the name and rate to come from the dialog_service_new dialog
                        EditText rateInput = (EditText) ((AlertDialog) dialog).findViewById(R.id.RateInput);
                        double rate = Double.parseDouble(rateInput.getText().toString());
                        args.putDouble("rate", rate);
                        //EditServiceDialogFragment.this.setArguments(args);
                        mListener.onDialogEdit(EditServiceDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle args = new Bundle();
                        //@anshu: get the name and rate to come from the dialog_service_new dialog
                        args.putString("name", (String)getArguments().get("name"));
                        //
                        EditServiceDialogFragment.this.setArguments(args);
                        mListener.onDialogDelete(EditServiceDialogFragment.this);
                    }
                })
        .setTitle((String)getArguments().get("name"));
        return builder.create();
    }
}
