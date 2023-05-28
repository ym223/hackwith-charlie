package jp.chocofac.charlie.data.service.location

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.chocofac.charlie.data.model.CollectionTags
import jp.chocofac.charlie.data.model.LikesData
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

    fun postLike(
        path: String,
        onSuccess: () -> Unit = {},
        onFailure: (e: Exception) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: ""
        fireStore.collection(CollectionTags.Posts.name)
            .document(path)
            .collection(CollectionTags.Likes.name)
            .document(uid).set(
                mapOf<String, String>()
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener(onFailure)
    }

    fun postUnLike(
        path: String,
        onSuccess: () -> Unit = {},
        onFailure: (e: Exception) -> Unit = {}
    ) {
        val uid = Firebase.auth.currentUser!!.uid
        fireStore.collection(CollectionTags.Posts.name)
            .document(path)
            .collection(CollectionTags.Likes.name)
            .document(uid)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener(onFailure)
    }

    fun listenLikes(
        path: String,
        onSuccess: (list: LikesData) -> Unit = {},
        onFailure: (e: Exception) -> Unit = {}
    ) {
        fireStore
            .collection(CollectionTags.Posts.name)
            .document(path)
            .collection(CollectionTags.Likes.name)
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                when {
                    e != null -> {
                        onFailure(e)
                        return@addSnapshotListener
                    }
                    snapshot == null -> {
                        onFailure(NoSuchElementException("Snapshot is not found."))
                    }
                    else -> {
                        val docs = snapshot.documents
                        val list = mutableListOf<String>()
                        for (doc in docs) {
                            list.add(doc.id)
                        }
                        onSuccess(LikesData(list))
                    }
                }
            }
    }

    fun isLikeContain(data: LikesData): Boolean {
        val uid = auth.currentUser?.uid ?: ""
        return data.isContain(uid)
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