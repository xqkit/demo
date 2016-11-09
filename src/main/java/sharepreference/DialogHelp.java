package sharepreference;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.flashtools.R;

/**
 * Created by dave.shi on 2016/5/11.
 */
public class DialogHelp {
    private Context context;
    public DialogHelp(Context context){
        this.context = context;
    }
    Dialog mDialog;
    View view;
    public void createNumDialog(TextView titleView, final TextView contentView,final String savedKey){
        if(mDialog ==null){
            mDialog = new Dialog(context);
            view = LayoutInflater.from(context).inflate(R.layout.num_dialog,null);
            mDialog.setContentView(view);
        }
        mDialog.show();
        TextView title = (TextView)view.findViewById(R.id.dialog_title);
        title.setText(titleView.getText().toString());
        final TextView content = (TextView)view.findViewById(R.id.num);
        content.setText(contentView.getText().toString());
        TextView ok = (TextView)view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.setText(content.getText().toString());
                SharePreferenceHelper.getInstance(context).saveChangeData(savedKey,Integer.parseInt(contentView.getText().toString()));
                if(mDialog!=null){
                    mDialog.dismiss();
                    mDialog = null;
                }
            }
        });
        TextView cancel = (TextView)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDialog!=null){
                    mDialog.dismiss();
                    mDialog = null;
                }
            }
        });
    }

}
