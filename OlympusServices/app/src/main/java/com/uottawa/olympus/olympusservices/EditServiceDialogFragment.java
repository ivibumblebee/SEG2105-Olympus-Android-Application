package com.uottawa.olympus.olympusservices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

public class EditServiceDialogFragment extends DialogFragment{

    /**
     * Creates a NoticeDialogListener interface for other classes to
     * implement to have this class be functional in the other classes.
     *
     */
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

    /**
     * Edit the Dialog to change rate of a service that the
     * admin wants to change.
     *
     * @param savedInstanceState Bundle to transfer information.
     * @return Dialog that is sent to admin for information.
     */
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
                        args.putString("name", (String)getArguments().get("name"));
                        EditText rateInput = (EditText) ((AlertDialog) dialog).findViewById(R.id.RateInput);
                        if(rateInput.getText().toString().length()>0){
                            Double rate = Double.parseDouble(rateInput.getText().toString());
                            args.putDouble("rate", rate);

                            EditServiceDialogFragment.this.setArguments(args);
                            mListener.onDialogEdit(EditServiceDialogFragment.this);
                        }
                        else{
                            Toast.makeText(getContext(), "Rate cannot be empty", Toast.LENGTH_LONG).show();

                        }

                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle args = new Bundle();
                        args.putString("name", (String)getArguments().get("name"));

                        EditServiceDialogFragment.this.setArguments(args);
                        mListener.onDialogDelete(EditServiceDialogFragment.this);
                    }
                })
        .setTitle((String)getArguments().get("name"));
        return builder.create();
    }
}
