package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etDescription, etAmount;
    Button btnAdd, btnDate;
    LinearLayout listLayout;
    TextView tvTotal, tvBottomCounter;

    ArrayList<Expense> expenseList = new ArrayList<>();
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        btnAdd = findViewById(R.id.btnAdd);
        btnDate = findViewById(R.id.btnDate);
        listLayout = findViewById(R.id.listLayout);
        tvTotal = findViewById(R.id.tvTotal);
        tvBottomCounter = findViewById(R.id.tvBottomCounter);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        btnDate.setText(day + "/" + (month + 1) + "/" + year);

        btnDate.setOnClickListener(v -> openDatePicker());
        btnAdd.setOnClickListener(v -> addExpense());

        calculateTotal();
    }

    private void openDatePicker() {
        new DatePickerDialog(this, (view, y, m, d) -> {
            year = y;
            month = m;
            day = d;
            btnDate.setText(d + "/" + (m + 1) + "/" + y);
            calculateTotal();
        }, year, month, day).show();
    }

    private void addExpense() {
        String desc = etDescription.getText().toString().trim();
        String amountText = etAmount.getText().toString().trim();

        if (desc.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(this, "Alanlar boş olamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                Toast.makeText(this, "Tutar pozitif olmalı", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Geçersiz tutar", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense(desc, amount, year, month, day);
        expenseList.add(expense);

        calculateTotal();

        etDescription.setText("");
        etAmount.setText("");
    }

    private void calculateTotal() {
        double selectedDayTotal = 0;
        double monthToDayTotal = 0;

        listLayout.removeAllViews();

        for (Expense e : expenseList) {

            // Seçili gün
            if (e.isSameDay(year, month, day)) {
                selectedDayTotal += e.amount;
                addExpenseView(e);
            }

            // Ay başından seçili güne kadar
            if (e.year == year && e.month == month && e.day <= day) {
                monthToDayTotal += e.amount;
            }
        }

        tvTotal.setText("Toplam: " + selectedDayTotal + " ₺");
        tvBottomCounter.setText("Ay Başından Toplam: " + monthToDayTotal + " ₺");
    }

    private void addExpenseView(Expense e) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(12, 12, 12, 12);

        TextView t1 = new TextView(this);
        t1.setText(e.description);
        t1.setTextSize(18); // ✅ açıklama fontu büyütüldü
        t1.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView t2 = new TextView(this);
        t2.setText(e.amount + " ₺");
        t2.setTextSize(18); // ✅ tutar fontu büyütüldü
        t2.setGravity(Gravity.END);

        row.addView(t1);
        row.addView(t2);

        listLayout.addView(row);
    }
}
