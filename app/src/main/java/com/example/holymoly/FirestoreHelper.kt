package com.example.holymoly

import android.util.Log
import com.example.holymoly.ui.drawer.TicketAdapter
import com.example.holymoly.ui.drawer.TicketInform
import com.example.holymoly.ui.tab.BucketDoneInform
import com.example.holymoly.ui.tab.BucketDoneItemAdapter
import com.example.holymoly.ui.tab.BucketInform
import com.example.holymoly.ui.tab.BucketItemAdapter
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreHelper {
    private val db = Firebase.firestore
    //firestore에서 이메일 가져오기
    val authHelper = AuthHelper()
    val userEmail = authHelper.getCurrentUserEmail()
    fun addUserToFirestore(email: String) {

        val data = hashMapOf("user_email" to email)

        db.collection("user").document(email)
            .set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    //버킷리스트 저장 (Do & Done)
    fun addBucketDoToFireStore(time: String, title: String, context: String, process: Boolean){
        val data = hashMapOf(
            "time" to time,
            "title" to title,
            "context" to context,
            "process" to process )

        db.collection("user").document(userEmail!!)
            .collection("bucketDo").document(time).set(data)
            .addOnSuccessListener { Log.d("DB", "$time/$title/$process") }
            .addOnFailureListener{ Log.d("DB", "Fail") }
    }

    fun addBucketDoneToFireStore(items: MutableSet<String>, adapter: BucketItemAdapter){
        val user = db.collection("user").document(userEmail!!)     //  현재 계정
        Log.d("DB", items.toString())
        for (time in items){ //bucketDo 에서 해당 time의 문서 가져와 Done에 저장
            Log.d("DB", "$time")
            user.collection("bucketDo")
                .document(time)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){ //db에서 데이터 가져오기 성공 시
                        val doc = task.result
                        if(doc != null && doc.exists()){ //해당 id의 문서가 존재하는 경우
                            val data = doc.data     //Do에서 데이터 가져오기

                            //Done에 저장
                            user.collection("bucketDone")
                                .document(time).set(hashMapOf("time" to time,
                                    "title" to (data?.get("title")?.toString()?: ""),
                                    "context" to (data?.get("context")?.toString()?: "")))
                                .addOnSuccessListener { Log.d("DB", "Done") }

                            //Do에서 해당 문서 삭제
                            user.collection("bucketDo")
                                .document(time).delete()
                                .addOnSuccessListener { Log.d("DB", "Do Delete : $time") }
                                .addOnFailureListener{ Log.d("DB", "Fail Delete : $time") }

                        }else{ //해당 문서가 존재하지 않는 경우
                            Log.d("DB", "not found")
                        }
                        Log.d("DB", "task success")
                    }else{ //db에서 데이터 가져오기 실패 시
                        Log.d("DB", "task fail")
                    }
                }
        }
        adapter.notifyDataSetChanged()
        items.clear()
    }


    //버킷리스트 가져오기 (Do & Done)
    fun getBucketDoToFireStore(adapter: BucketItemAdapter):List<BucketInform>{
        var informs : MutableList<BucketInform> = mutableListOf()

        db.collection("user").document(userEmail!!)
            .collection("bucketDo")
            .addSnapshotListener{ qsnap, e ->
                informs.clear()
                if (qsnap != null) {
                    for(doc in qsnap!!.documents){
                        val time = doc["time"].toString()
                        val title = doc["title"].toString()
                        val context = doc["context"].toString()
                        val process = doc["process"].toString().toBoolean()
                        informs.add(BucketInform(time, title, context, process))
                    }
                }
                adapter.notifyDataSetChanged()
            }
        return informs
    }

    fun getBucketDoneToFireStore(adapter: BucketDoneItemAdapter):List<BucketDoneInform>{
        var informs : MutableList<BucketDoneInform> = mutableListOf()

        db.collection("user").document(userEmail!!)
            .collection("bucketDone")
            .addSnapshotListener{ qsnap, e ->
                informs.clear()
                if(qsnap != null) {
                    for(doc in qsnap!!.documents){
                        val time = doc["time"].toString()
                        val title = doc["title"].toString()
                        val context = doc["context"].toString()
                        informs.add(BucketDoneInform(time, title, context))
                    }
                }
                adapter.notifyDataSetChanged()
            }
        return informs
    }

    //선택된 항목들 진행시키기
    fun modifyBucketDoToFireStore(items: MutableSet<String>, adapter: BucketItemAdapter){
        for(time in items){
            db.collection("user").document(userEmail!!)
                .collection("bucketDo").document(time)
                .update("process", "true")
        }
        adapter.notifyDataSetChanged()
        items.clear()
    }

    //버킷리스트 삭제하기 (Do & Done)
    fun deleteBucketDoToFireStore(items: MutableSet<String>, adapter:BucketItemAdapter) : String{
        var message : String = ""

        //아이템 삭제
        for(time in items){
            db.collection("user").document(userEmail!!)
                .collection("bucketDo").document(time)
                .delete()
                .addOnSuccessListener { message = "success" }  //성공
                .addOnFailureListener { e ->
                    message = if(e is FirebaseFirestoreException
                        && e.code == FirebaseFirestoreException.Code.NOT_FOUND){
                        "notFound"  //해당 아이템이 존재하지 않음
                    } else{
                        "error"     //다른 예외
                    }
                }
        }
        adapter.notifyDataSetChanged()
        items.clear()
        return message
    }

    fun deleteBucketDoneToFireStore(items: MutableSet<String>, adapter:BucketDoneItemAdapter) : String{
        var message : String = ""

        //아이템 삭제
        for(time in items){
            db.collection("user").document(userEmail!!)
                .collection("bucketDone").document(time)
                .delete()
                .addOnSuccessListener { message = "success" }  //성공
                .addOnFailureListener { e ->
                    message = if(e is FirebaseFirestoreException
                        && e.code == FirebaseFirestoreException.Code.NOT_FOUND){
                        "notFound"  //해당 아이템이 존재하지 않음
                    } else{
                        "error"     //다른 예외
                    }
                }

        }
        adapter.notifyDataSetChanged()
        items.clear()
        return message
    }

    fun storeTicketToFireStore(time: String, departCountry: String, arriveCountry : String, departDate : String, arriveDate:String){
        //국제선 or 국내선
        val domList = setOf("CJU", "PUS", "TAE", "ICN", "GMP", "RSU", "USN")
        val type : String = if(arriveCountry in domList && departCountry in domList) "domestic" else  "international"

        //필드 생성
        val data = hashMapOf(
            "time" to time,
            "type" to type,
            "departCountry" to departCountry,
            "arriveCountry" to arriveCountry,
            "departDate" to departDate,
            "arriveDate" to arriveDate )

        //문서 생성
        db.collection("user").document(userEmail!!)
            .collection("myFlight").document(time).set(data)
            .addOnSuccessListener { Log.d("DB", "$time/$type/$departCountry/$departDate") }
            .addOnFailureListener{ Log.d("DB", "Fail") }
    }

    fun getTicketFromFireStore(adapter: TicketAdapter):List<TicketInform>{
        var informs : MutableList<TicketInform> = mutableListOf()

        db.collection("user").document(userEmail!!)
            .collection("myFlight")
            .addSnapshotListener{ qsnap, e ->
                informs.clear()
                for(doc in qsnap!!.documents){
                    val time = doc["time"].toString()
                    val type = doc["type"].toString()
                    val departDate = doc["departDate"].toString()
                    val arriveDate = doc["arriveDate"].toString()
                    val departCountry = doc["departCountry"].toString()
                    val arriveCountry = doc["arriveCountry"].toString()
                    informs.add(TicketInform(time, type, departDate, arriveDate, departCountry, arriveCountry))
                }
                adapter.notifyDataSetChanged()
            }
        return informs
    }

    fun deleteTicketFromFireStore(items: MutableSet<String>, adapter: TicketAdapter) : String{
        //함수 실행 결과 메세지
        var message : String = ""

        //아이템 삭제
        for(time in items){
        db.collection("user").document(userEmail!!)
            .collection("myFlight").document(time)
            .delete()
            .addOnSuccessListener { message = "success" }  //성공
            .addOnFailureListener { e ->
                message = if(e is FirebaseFirestoreException
                    && e.code == FirebaseFirestoreException.Code.NOT_FOUND){
                    "notFound"  //해당 아이템이 존재하지 않음
                } else{
                    "error"     //다른 예외
                }
            }

        }
        adapter.notifyDataSetChanged()
        return message
    }


    fun addHolidayToFirestore(holiday_title:String, start_year: Int, start_month:Int, start_date:Int,
                              end_year:Int, end_month:Int, end_date:Int, category:Int) {
        val data = hashMapOf(
            "holiday_title" to holiday_title,
            "start_year" to start_year,
            "start_month" to start_month,
            "start_date" to start_date,
            "end_year" to end_year,
            "end_month" to end_month,
            "end_date" to end_date,
            "category" to category)

        db.collection("user").document(userEmail!!).
        collection("holiday").document(holiday_title).set(data)
            .addOnSuccessListener { documentReference ->
                // 성공적으로 데이터 추가됨
                Log.d(TAG, "Document successfully added with autogenerated ID: $documentReference")
            }
            .addOnFailureListener { e ->
                // 데이터 추가 실패
                Log.w(TAG, "Error adding document", e)
            }

    }


    fun getMonthHolidaysFromFirestore(this_month:Int, callback: (List<Map<String, Any>>) -> Unit) {
        val startQuery = db.collection("user")
            .document(userEmail!!)
            .collection("holiday")
            .whereEqualTo("start_month", this_month)

        val endQuery = db.collection("user")
            .document(userEmail!!)
            .collection("holiday")
            .whereEqualTo("end_month", this_month)

        // 문서가 변경될 때마다 콜백 함수 실행
        startQuery.addSnapshotListener { documents_start, startException -> // 문서 실시간 확인
            var holidayList = mutableListOf<Map<String, Any>>()
            if (startException != null) {
                Log.w(TAG, "Error getting start documents: ", startException)
                callback(emptyList())
                return@addSnapshotListener
            }

            documents_start?.let {
                for (document in it) {
                    val data = document.data
                    holidayList.add(data)
                }

                endQuery.addSnapshotListener { documents_end, endException ->
                    if (endException != null) {
                        Log.w(TAG, "Error getting end documents: ", endException)
                        callback(holidayList)
                        return@addSnapshotListener
                    }

                    documents_end?.let { endDocs ->
                        for (document in endDocs) {
                            val data = document.data
                            if (!holidayList.contains(data)) {
                                holidayList.add(data)
                            }
                        }
                        callback(holidayList)
                    }
                }
            }
        }
    }

    suspend fun getAllHolidaysFromFirestore(): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val documents = db.collection("user")
                    .document(userEmail!!)
                    .collection("holiday")
                    .get()
                    .await()

                val holidayList = mutableListOf<Map<String, Any>>()

                for (document in documents) {
                    // 각 문서에 대한 처리
                    val data = document.data
                    // data를 사용하여 필요한 작업 수행
                    holidayList.add(data)
                }

                return@withContext holidayList
            } catch (exception: Exception) {
                // 쿼리 실패 시 처리
                Log.w(TAG, "Error getting documents: ", exception)
                return@withContext emptyList() // 실패할 경우 빈 리스트 반환 또는 예외처리 방식에 따라 변경
            }
        }
    }


    fun deleteHolidaysFromFirestore(delete_title: String) {
        db.collection("user")

                .document(userEmail!!)
                .collection("holiday")
                .document(delete_title)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

        //upcomingschedules 어댑터랑 연결시켜주려고 했는데 잘 안되는둣...?
        //adapter.notifyDataSetChanged()

    }

    companion object {
        private const val TAG = "FirestoreHelper"
    }

}