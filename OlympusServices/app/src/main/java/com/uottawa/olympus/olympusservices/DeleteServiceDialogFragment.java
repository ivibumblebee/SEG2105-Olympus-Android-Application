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

public class DeleteServiceDialogFragment extends DialogFragment{
    public interface NoticeDialogListener {
        public void onDialogDelete(DialogFragment dialog);
        public void onDialogNevermind(DialogFragment dialog);
    }
    DeleteServiceDialogFragment.NoticeDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DeleteServiceDialogFragment.NoticeDialogListener) context;
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
        builder.setView(inflater.inflate(R.layout.dialog_service_delete, null))
                // Add action buttons
                .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle args = new Bundle();
                        args.putString("name", (String)getArguments().get("name"));
                        DeleteServiceDialogFragment.this.setArguments(args);
                        mListener.onDialogDelete(DeleteServiceDialogFragment.this);


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle args = new Bundle();
                        args.putString("name", (String)getArguments().get("name"));
                        mListener.onDialogNevermind(DeleteServiceDialogFragment.this);
                    }
                })
                .setTitle((String)getArguments().get("name"));
        return builder.create();
    }
}
