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

import cn.edu.seu.cse.seualarm.R;

/**
 * Created by Coder Geass on 2016/12/17.
 */

public class RunAlreadyPopWindow implements View.OnClickListener{
    private Button cel_btn, set_btn;
    private PopupWindow mPopupWindow;
    private Context mContext;
//    public AlertPopWindow.AlertListner alertListner;

    public RunAlreadyPopWindow(Context context) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_window_run_already, null);
        set_btn = (Button) view.findViewById(R.id.run_ok);

        set_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.delete_cancel:
//                alertListner.obtainMessage(0);
//                dismiss();
//                break;
            case R.id.run_ok:
                dismiss();
                break;
            default:
                break;
        }
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
