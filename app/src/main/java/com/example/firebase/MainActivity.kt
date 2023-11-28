package com.example.firebase


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.ui.theme.FirebaseTheme
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background

                ) {
                        firebaseUI(LocalContext.current)
                    }
                }
            }
        }
    }


@Composable
fun firebaseUI(context: Context) {

    //zmienne przechowujące dane z textfield
    val Name = remember {
        mutableStateOf("")
    }

    val Surname = remember {
        mutableStateOf("")
    }

    val Email = remember {
        mutableStateOf("")
    }

    val Birthday = remember {
        mutableStateOf("")
    }

    //ustawienie columny i wszystkich widgetow
    Column(
        //
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.Magenta),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Baza danych",
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            fontSize = 45.sp, modifier = Modifier.padding(5.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            //przypisanie wartosci z text fielda
            value = Name.value,

            //gdy wartosc sie zmieni przypisujemy ją do zmiennej name
            onValueChange = { Name.value = it },

            placeholder = { Text(text = "Wpisz swoje imię:") },

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            //wszyskto co wpiszemy ma sie zawierac w jednej linii
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(

            value = Surname.value,

            onValueChange = { Surname.value = it },

            placeholder = { Text(text = "Wpisz swoje nazwisko:") },

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),


            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(

            value = Email.value,

            onValueChange = { Email.value = it },

            placeholder = { Text(text = "Podaj adres E-Mail:") },

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(

            value = Birthday.value,

            onValueChange = { Birthday.value = it },

            placeholder = { Text(text = "Podaj date urodzin:") },

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            singleLine = true,
        )


        Spacer(modifier = Modifier.height(10.dp))

        Button(


            onClick = {
                // upewniamy sie czy wszystkie dane sa wpisane
                if (TextUtils.isEmpty(Name.value.toString())) {
                    Toast.makeText(context, "Proszę podaj imie...", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(Surname.value.toString())) {
                    Toast.makeText(context, "Proszę podaj nazwisko...", Toast.LENGTH_SHORT)
                        .show()
                } else if (TextUtils.isEmpty(Email.value.toString())) {
                    Toast.makeText(context, "Proszę podaj adres E-Mail...", Toast.LENGTH_SHORT)
                        .show()
                }else if (TextUtils.isEmpty(Birthday.value.toString())) {
                    Toast.makeText(context, "Proszę podaj date urodzin...", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    //jesli wszystkie sa wpisane wysylamy do firestora
                    addDataToFirebase(
                        Name.value,
                        Surname.value,
                        Email.value ,
                        Birthday.value,
                        context
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White)


        ) {
            Text(text = "Wyślij dane", modifier = Modifier.padding(8.dp))
        }
    }
}

fun addDataToFirebase(
    Name: String,
    Surname: String,
    Email: String,
    Birthday: String,
    context: Context
) {
    // tworzymy instancję bazy danych
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    // teraz tworzymy kolekcję danych w naszej bazie
    val dbUData: CollectionReference = db.collection("Dane użytkownika")
    //stworzenienie zmiennej val, ktora przechowuje nasz obiekt data
    val udata = Data(Name, Surname, Email, Birthday)

    //dodanie danych do firebase
    dbUData.add(udata).addOnSuccessListener {

        Toast.makeText(context, "Twoje dane zostały dodane do bazy Firestore", Toast.LENGTH_SHORT).show()

    }.addOnFailureListener { e ->

        Toast.makeText(context, "Niepowodzenie wysyłyania danych!\n$e", Toast.LENGTH_SHORT).show()

    }

}
