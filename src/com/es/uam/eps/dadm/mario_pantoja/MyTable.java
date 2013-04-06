package com.es.uam.eps.dadm.mario_pantoja;

import android.content.Context;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MyTable extends TableLayout
{
    public MyTable(Context context) {
        super(context);

        setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        TableRow row = new TableRow(context);
        row.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        Button b = new Button(getContext());
        b.setText("hello");
        b.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(b); 
        addView(row);
    }
}