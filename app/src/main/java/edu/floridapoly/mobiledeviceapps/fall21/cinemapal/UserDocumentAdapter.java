package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDocumentAdapter extends ListAdapter<UserDocumentAdapter.UserDocument, UserDocumentAdapter.UserDocumentHolder> {
    public interface OnClickListener {
        void onClick(UserDocument user, UserDocumentHolder holder);
    }
    public interface OnBindView {
        void onBind(UserDocument user, UserDocumentHolder viewHolder);
    }

    private String buttonText = "SELECT";
    private ArrayList<OnClickListener> onClick = new ArrayList();
    private ArrayList<OnBindView> onBind = new ArrayList();

    public UserDocumentAdapter() {
        super(new DiffUtil.ItemCallback<UserDocument>() {
            @Override
            public boolean areItemsTheSame(@NonNull UserDocument oldItem, @NonNull UserDocument newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull UserDocument oldItem, @NonNull UserDocument newItem) {
                return oldItem.bio.equals(newItem.bio);
            }
        });
    }

    @NonNull
    @Override
    public UserDocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_document_item, parent, false);
        return new UserDocumentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDocumentHolder holder, int position) {
        UserDocument user = getItem(position);
        holder.bio.setText(user.bio);

        UserDocumentAdapter adapter = this;
        holder.select.setText(buttonText);
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(OnClickListener onClick : adapter.onClick) {
                    onClick.onClick(user, holder);
                }
            }
        });
        for(OnBindView onBind : this.onBind) {
            onBind.onBind(user, holder);
        }
    }

    public void setButtonText(String text) {
        this.buttonText = text;
    }
    public void setOnClickListener(UserDocumentAdapter.OnClickListener onClick) {
        this.onClick.add(onClick);
    }
    public void setOnBind(UserDocumentAdapter.OnBindView onBind) {
        this.onBind.add(onBind);
    }


    public static class UserDocument implements Serializable {
        String id;
        String username;
        String bio;

        public UserDocument(String id, String username, String bio) {
            this.id = id;
            this.username = username;
            this.bio = bio;
        }
    }

    public class UserDocumentHolder extends RecyclerView.ViewHolder {
        TextView bio;
        Button select;

        public UserDocumentHolder(@NonNull View itemView) {
            super(itemView);
            bio = itemView.findViewById(R.id.userBioView);
            select = itemView.findViewById(R.id.thisMeButton);
        }
    }
}
