package studio.roboto.hack24.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    public static FirebaseManager sharedInstance = new FirebaseManager();

    public static final String FIR_ROOT = "***REMOVED***/";

    public final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

}
