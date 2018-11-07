package com.uottawa.olympus.olympusservices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

public class NewServiceDialogFragment extends DialogFragment {


    public interface NoticeDialogListener {
        public void onDialogNew(DialogFragment dialog);
        public void onDialogNevermind(DialogFragment dialog);
    }
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    //String name = ((EditText) view.findViewById(R.id.NameInput)).getText().toString();
    //int rate = Integer.parseInt(((EditText) view.findViewById(R.id.RateInput)).getText().toString())
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_service_new, null);
        builder.setView(view);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText nameInput = (EditText) ((AlertDialog) dialog).findViewById(R.id.NameInput);
                        EditText rateInput = (EditText) ((AlertDialog) dialog).findViewById(R.id.RateInput);
                        String name = nameInput.getText().toString();
                        double rate = Double.parseDouble(rateInput.getText().toString());
                        Bundle args = new Bundle();
                        args.putString("name", name);
                        args.putDouble("rate", rate);
                        NewServiceDialogFragment.this.setArguments(args);
                        mListener.onDialogNew(NewServiceDialogFragment.this);
                        //@anshu: get the name and rate to come from the dialog_service_new dialog
                        dostuff();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewServiceDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void dostuff(){


    }


}
