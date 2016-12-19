package cn.edu.seu.cse.seualarm.controler;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.edu.seu.cse.seualarm.R;

/**
 * Created by Coder Geass on 2016/12/13.
 */

public class AlertPopWindow implements View.OnClickListener{
    private Button cel_btn, set_btn;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private TextView hint;
    public AlertListner alertListner;

    public AlertPopWindow(Context context) {
        this.mContext = context;

        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initView());
        mPopupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopupWindow.setFocusable(false);
                // mPopupWindow.dismiss();
                return true;
            }
        });
    }

    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_window_alarm_alert, null);
        cel_btn = (Button) view.findViewById(R.id.delete_cancel);
        set_btn = (Button) view.findViewById(R.id.delete_ok);
        hint = (TextView) view.findViewById(R.id.tv_hint);

        cel_btn.setOnClickListener(this);
        set_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_cancel:
                alertListner.obtainMessage(0);
                dismiss();
                break;
            case R.id.delete_ok:
                alertListner.obtainMessage(1);
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface AlertListner {
        void obtainMessage(int flag);
    }

    public void setOnAlertListenr(AlertListner alertListner) {
        this.alertListner = alertListner;
    }

    public void setHint(String str) {
        if (hint != null)
            hint.setText(str);
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
}
