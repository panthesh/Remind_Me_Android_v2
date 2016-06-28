package codepath.shah.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private int position = 0;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String itemText = getIntent().getStringExtra("itemText");
        int code = getIntent().getIntExtra("code", 0);
        position = getIntent().getIntExtra("position", 0);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(itemText);
        editText.setCursorVisible(true);
        editText.setSelection(editText.getText().length());
        setSupportActionBar(toolbar);
        setUpSaveButtonListener(itemText);
    }

    private void setUpSaveButtonListener(final String oldText) {

        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                EditText editText = (EditText) findViewById(R.id.editText);
                data.putExtra("itemText", oldText);
                data.putExtra("editedText", editText.getText().toString());
                data.putExtra("position", position);
                data.putExtra("code", 200);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
