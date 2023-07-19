package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import com.example.cryptonotifier.R;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomListAdapter extends ArrayAdapter<ListItem> {
    private ArrayList<ListItem> originalItems = new ArrayList<>();

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull List<ListItem> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        ListItem currentItem = getItem(position);

        if (currentItem != null) {
            TextView textView = convertView.findViewById(R.id.textView);
            Switch switchView = convertView.findViewById(R.id.switchView);

            textView.setText(currentItem.getText());
            switchView.setChecked(currentItem.isSwitchState());

//            switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                currentItem.setSwitchState(isChecked);
//                // Save the switched state to SharedPreferences and update originalItems
//                saveSwitchState(currentItem.getText(), isChecked, originalItems);
//            });
        }

        return convertView;
    }

    public void notifyDataChanged(List<ListItem> updatedItems) {
        List<ListItem> toUpdateItems = new ArrayList<>(updatedItems); // Создаем копию updatedItems
        clear();
        addAll(toUpdateItems);
        notifyDataSetChanged();
    }

//    private void saveSwitchState(String symbol, boolean switchState, ArrayList<ListItem> originalItems) {
//        System.out.println("Saved");
//
//        // Update the switch state in the originalItems list
//        for (ListItem item : originalItems) {
//            if (item.getText().equals(symbol)) {
//                item.setSwitchState(switchState);
//                break;
//            }
//        }
//
//        SharedPreferences sharedPrefs = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//
//        if (switchState) {
//            // Save the switch state to SharedPreferences
//            editor.putBoolean(symbol, switchState);
//        } else {
//            // If switch is off, remove the key from SharedPreferences
//            editor.remove(symbol);
//        }
//
//        editor.apply();
//
//        // Debug print to check the contents after saving/removing the key
//        Map<String, ?> allEntries = sharedPrefs.getAll();
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            System.out.println(key + ": " + value);
//        }
//    }
}

