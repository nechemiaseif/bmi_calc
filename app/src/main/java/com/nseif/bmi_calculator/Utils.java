package com.nseif.bmi_calculator;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Utils {
    /**
     * Shows an Android (nicer) equivalent to JOptionPane
     *  @param strTitle Title of the Dialog box
     * @param strMsg   Message (body) of the Dialog box
     */
    public static void showInfoDialog(Context context, int strTitle, int strMsg) {
        // create the listener for the dialog
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        // Create the AlertDialog.Builder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Use the AlertDialog's Builder Class methods to set the title, icon, message, et al.
        // These could all be chained as one long statement, if desired
        alertDialogBuilder.setTitle(strTitle);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setMessage(strMsg);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNeutralButton(context.getString(android.R.string.ok), listener);

        // Create and Show the Dialog
        alertDialogBuilder.show();
    }

}
