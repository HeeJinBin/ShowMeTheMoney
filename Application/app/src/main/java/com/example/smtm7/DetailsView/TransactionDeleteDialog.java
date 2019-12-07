package com.example.smtm7.DetailsView;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.example.smtm7.R;

public class TransactionDeleteDialog {
    private Context context;
    OnMyDialogResult dialogResult;

    public TransactionDeleteDialog(Context context){
        this.context = context;
    }

    public void callFunction(){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.delete_transaction_dialog);

        dialog.show();

        final Button btn_delete = dialog.findViewById(R.id.transaction_delete);
        final Button btn_cancel = dialog.findViewById(R.id.transaction_cancel);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogResult != null){
                    dialogResult.finish(true);
                }
                DetailsActivity.screenCheck = true;
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogResult != null){
                    dialogResult.finish(false);
                }
                DetailsActivity.screenCheck = true;
                dialog.dismiss();
            }
        });
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        this.dialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(boolean result);
    }
}
