package edu.upc.mcia.publications.ui.publications;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jakewharton.rxbinding.widget.RxAdapterView;

import edu.upc.mcia.publications.R;
import timber.log.Timber;

public class FilterDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View content = View.inflate(getActivity(), R.layout.dialog_publications_filter, null);


        Spinner spinner = (Spinner) content.findViewById(R.id.typeFilter2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RxAdapterView.itemSelections(spinner)
                .subscribe(i -> Timber.d("Item selected"));

        AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                .setTitle(R.string.title_dialog_filter)
                .setView(content)
                .setPositiveButton(R.string.action_apply_filter, (dialogInterface, i) -> Timber.d("Filters applied"))
                .setNegativeButton(R.string.action_dismiss_dialog, (dInterface, i) -> Timber.d("Dialog dismissed"))
                .create();

        return dialog;
    }

}
