package jp.chocofac.charlie.data.service.location

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import jp.chocofac.charlie.data.model.CollectionTags
import jp.chocofac.charlie.data.model.PostData
import jp.chocofac.charlie.data.model.fromMapToPostData
import javax.inject.Inject

class FireStoreRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun postSenryuData(
        data: PostData,
        onSuccess: (dr: DocumentReference) -> Unit = {},
        onFailure: (e: Exception) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: ""
        fireStore.collection(CollectionTags.Posts.name)
            .add(data.copy(contributorId = uid))
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun fetchCollection(
        onSuccess: (collection: List<PostData>) -> Unit = {},
        onError: (e: Exception) -> Unit = {}
    ) {
        fireStore.collection(CollectionTags.Posts.name)
            .addSnapshotListener { snapshot, e ->
                when {
                    e != null -> {
                        onError(e)
                        return@addSnapshotListener
                    }
                    snapshot == null -> {
                        onError(IllegalStateException("Snapshot is not found."))
                    }
                    snapshot.isEmpty -> {
                        onError(NoSuchElementException("Snapshot is not found."))
                    }
                    else -> {
                        val collection = snapshot.documents.map {
                            val data = it.data
                            if (data.isNullOrEmpty()) {
                                PostData()
                            } else {
                                data.fromMapToPostData()
                            }
                        }
                        onSuccess(collection)
                    }
                }
            }
    }
}