package com.example.btl.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.btl.fragments.DetailField;

public class DetailFragentAdapterField extends FragmentStateAdapter {

    public DetailFragentAdapterField(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Tạo các fragment tương ứng với mỗi position
        switch (position) {
            case 0:
                return new DetailField(); // Fragment 1
            case 1:
                return new DetailField(); // Fragment 2 (có thể thay đổi fragment nếu cần)
            case 2:
                return new DetailField();
            case 3:
                return new DetailField();
            default:
                return new DetailField(); // Mặc định trả về fragment 1
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Số lượng fragment bạn muốn trong ViewPager2
    }
}
