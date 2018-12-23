package parmar.nishikant.livechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import parmar.nishikant.livechatapp.Fragments.ChatsFragment;
import parmar.nishikant.livechatapp.Fragments.MyProfileFragment;
import parmar.nishikant.livechatapp.Fragments.SUserFragment;
import parmar.nishikant.livechatapp.Fragments.UserFragment;
import parmar.nishikant.livechatapp.Model.User;

public class MainActivity extends AppCompatActivity {
    CircleImageView dpimg;
    TextView fullname;
    FirebaseUser firebaseUser,firebaseUser2;
    DatabaseReference reference,reference2;
    Toolbar toolbar;
    String check = "YES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Live Chat App");
        dpimg = findViewById(R.id.profileimage);
        fullname = findViewById(R.id.FN);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user_info").child(firebaseUser.getUid());
        ValueEventListener userinfolistner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                /*fullname.setText(user.getFullname());
                if(user.getDpURL().equals("default")){
                    dpimg.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MainActivity.this).load(user.getDpURL()).into(dpimg);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(userinfolistner);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new UserFragment(),"Users");
        viewPagerAdapter.addFragment(new MyProfileFragment(),"Me");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    protected void onPause() {
        super.onPause();
        setStatus("offline");
    }
    @Override

    protected void onResume() {
        super.onResume();
        setStatus("online");
        check="YES";
    }
    private void setStatus(String s){
        if(!check.equals("NO")){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user_info/"+firebaseUser.getUid()).child("status");
        reference.setValue(s); }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.loginBtn:
                check = "NO";
                FirebaseDatabase.getInstance().getReference("user_info/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
                Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
                //break;
            case R.id.myprofile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                return true;
                //break;
        }
        return false;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        @Override
        public Fragment getItem(int position){
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }
        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }
    }
}
