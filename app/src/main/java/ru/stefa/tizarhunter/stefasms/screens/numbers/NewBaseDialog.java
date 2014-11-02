package ru.stefa.tizarhunter.stefasms.screens.numbers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.stefa.tizarhunter.stefasms.database.DatabaseActions;

public class NewBaseDialog extends AlertDialog.Builder
{
    private int mChoise = -1;
    private TextView mTitle;
    private EditText mEditText;

    public NewBaseDialog(final Context context, final Callback callback)
    {
        super(context);
        mTitle = createTitle(context);
        mTitle.setText("Новая база номеров");
        LinearLayout linearLayout = createMainLayout(context);
        TextView textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_Small);
        textView.setText("Введите имя базы номеров");
        linearLayout.addView(textView);
        mEditText = new EditText(context);
        linearLayout.addView(mEditText);
        List<String> stringList = new ArrayList<String>();
        stringList.add("Ввести номера вручную");
        stringList.add("Импортировать из файла");
        linearLayout.addView(createRadioButtons(context, stringList));
        setCustomTitle(mTitle).setView(linearLayout).setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        DatabaseActions databaseActions = new DatabaseActions();
                        databaseActions.connectionDatabase(context);
                        databaseActions.createTable("dialog");
                        if (mChoise == 0)
                        {
                            callback.ok(mEditText.getText().toString());
                        }
                        else
                        {
                            callback.okImport(mEditText.getText().toString());
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null);

    }

    private TextView createTitle(Context context)
    {
        TextView textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_DialogWindowTitle);
        return textView;
    }

    private LinearLayout createMainLayout(Context context)
    {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }


    private RadioGroup createRadioButtons(Context context, List<String> stringList)
    {
        RadioGroup radioGroup = new RadioGroup(context);
        for (int i = 0; i < stringList.size(); i++)
        {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(stringList.get(i));
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                mChoise = i;
            }
        });
        return radioGroup;
    }

    private TextView createTextView(Context context, int style)
    {
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, style);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(15, 0, 0, 0);
        return textView;
    }

    public static interface Callback
    {
        void ok(String nameBase);
        void okImport(String nameBase);
    }
}
