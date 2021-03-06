package com.gupta.pocketfull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gupta.pocketfull.bean.Balance;
import com.gupta.pocketfull.bean.CurrentBalanace;
import com.gupta.pocketfull.database.DBHandler;
import com.gupta.pocketfull.bean.Pockets;

public class AddBalance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Pockets pocket = new  Pockets();

    Bundle bundle;

    CurrentBalanace currentBalanace = new CurrentBalanace();

    Balance balance = new Balance();

    int pocketId;

    Button addNewBalance;
    EditText balanceAmount;
    EditText balanceDate;
    EditText balanceDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        balanceAmount = (EditText) findViewById(R.id.add_balance_amount);
        balanceDate = (EditText) findViewById(R.id.add_balance_date);
        balanceDetail = (EditText) findViewById(R.id.add_balance_detail);
        addNewBalance = (Button) findViewById(R.id.add_new_balance_button);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Intent login = getIntent();
        bundle = login.getExtras();

        pocket.setPocketId(bundle.getInt("ID"));
        pocket.setName(bundle.getString("NAME"));
        pocket.setDate(bundle.getString("DATE"));
        pocket.setLastResetDate(bundle.getString("RESETDATE"));
/*
        TextView pocket_name_navbar = (TextView)findViewById(R.id.pocket_name_navbar);
        pocket_name_navbar.setText("" + pocket.getName());
*/
        pocketId = pocket.getPocketId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addNewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                balance.setAmount(Integer.parseInt(balanceAmount.getText().toString()));
                balance.setDetails(balanceDetail.getText().toString());
                balance.setDate(balanceDate.getText().toString());
                balance.setPocketId(pocketId);

                DBHandler db = new DBHandler(AddBalance.this, null, null, 1);

                currentBalanace = db.getLatestCurrentBalance(pocketId);
                currentBalanace.setPocketId(pocketId);
                int sum = currentBalanace.getAmount() + balance.getAmount();

                Log.d("AddBalance.Java: ", "PocketID : "+ pocketId +" currentBalanace.getAmount(): " + currentBalanace.getAmount() + " balance.getAmount():"
                + balance.getAmount() + " Sum:" + sum);

                currentBalanace.setAmount(sum);

                db.addCurrentBalance(currentBalanace);
                db.addBalance(balance);

                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_balance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.add_balance) {
            Intent intent = new Intent(getApplicationContext(), AddBalance.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(getApplicationContext(), BalanceHistory.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.expenses) {
            Intent intent = new Intent(getApplicationContext(), Expenses.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.setting) {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.logout) {
            Intent intent = new Intent(getApplicationContext(), Pocket.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
