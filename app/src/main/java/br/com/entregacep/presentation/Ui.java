package br.com.entregacep.presentation;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
final class Ui {
    static final int INK=Color.rgb(18,43,54), TEAL=Color.rgb(0,103,105);
    static LinearLayout page(Activity a,String title,String subtitle) {
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(24,36,24,20); root.setBackgroundColor(Color.rgb(246,248,248));
        TextView brand=text(a,"ENTREGA CEP",14); brand.setTextColor(TEAL); root.addView(brand);
        TextView heading=text(a,title,28); heading.setPadding(0,20,0,8); root.addView(heading);
        root.addView(text(a,subtitle,15)); a.setContentView(root); return root;
    }
    static TextView text(Activity a,String value,int size) {TextView t=new TextView(a);t.setText(value);t.setTextSize(size);t.setTextColor(INK);t.setPadding(0,6,0,6);return t;}
    static Button button(Activity a,LinearLayout parent,String title,View.OnClickListener listener) {Button b=new Button(a);b.setText(title);b.setAllCaps(false);b.setOnClickListener(listener);parent.addView(b);return b;}
    static EditText field(Activity a,LinearLayout parent,String label,int id) {parent.addView(text(a,label,14)); EditText e=new EditText(a);e.setId(id);e.setSingleLine(true);e.setTextSize(16);parent.addView(e,new LinearLayout.LayoutParams(-1,-2));return e;}
}
