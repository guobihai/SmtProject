package com.smtlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.smtlibrary.R;


/**
 * Created by gbh on 16/8/11.
 * 密码认证对话框
 *
 * @author gbh
 */
public class PassWdDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private EditText input;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;

    public PassWdDialog(Context context) {
        super(context, R.style.alert_dialog);
        mContext = context;
    }

    public void setCancelClickListener(OnSweetClickListener mCancelClickListener) {
        this.mCancelClickListener = mCancelClickListener;
    }

    public void setConfirmClickListener(OnSweetClickListener mConfirmClickListener) {
        this.mConfirmClickListener = mConfirmClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smt_dialog_pw_view);
        findViewById(R.id.dCancle).setOnClickListener(this);
        findViewById(R.id.dOk).setOnClickListener(this);
        input = (EditText) findViewById(R.id.search_input);
        input.setFocusable(true);
        input.requestFocus();
        input.setFocusableInTouchMode(true);
        setCanceledOnTouchOutside(false);
        input.setOnEditorActionListener(editorActionListener);
    }

    public void setError(String str) {
        if (TextUtils.isEmpty(str))
            input.setError("密码错误");
        else
            input.setError(str);
        input.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dCancle) {
            if (null != mCancelClickListener) {
                mCancelClickListener.onClick(this, input.getText().toString());
            } else
                dismiss();
        } else if (view.getId() == R.id.dOk) {
            if (null != mConfirmClickListener) {
                mConfirmClickListener.onClick(this, input.getText().toString());
            } else
                dismiss();
        }

    }

    public interface OnSweetClickListener {
        void onClick(PassWdDialog inputDialog, String value);
    }


    /**
     * 键盘监听
     */
    public OnEditorActionListener editorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (null != mConfirmClickListener) {
                    mConfirmClickListener.onClick(PassWdDialog.this, input.getText().toString());
                } else
                    dismiss();
            }
            return true;
        }
    };
}
