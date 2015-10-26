package com.micocards.cclj.micocards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

/*
* MicoDbAdapter.java
*
* Version 1
*
* 03/03/2015
*
* @author Conor Keenan, x13343806
* @author Lee Redmond, x13488632
* @author Jakub Nazar, x13446722
* @author Conor Donnelly, x13734595
*/
public class MicoDbAdapter {
    MicoDbHelper dbHelper;

    public MicoDbAdapter(Context context) {
        dbHelper = new MicoDbHelper(context);
    }

    public void addUser(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(dbHelper.U_NAME, name);
        values.put(dbHelper.U_T_SCORE, 0);
        values.put(dbHelper.U_T_COUNT, 1);
        values.put(dbHelper.U_S_COUNT, 1);
        values.put(dbHelper.U_S_SCORE, 0);
        values.put(dbHelper.U_isACTIVE, 0);


        db.insert(dbHelper.U_TABLE_NAME, null, values);
    }

    public int getTriviaScore() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int score;

        String[] columns = {dbHelper.U_T_SCORE};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, dbHelper.U_isACTIVE + " = " + 1, null, null, null, null);

        c.moveToFirst();

        int userIndex = c.getColumnIndex(dbHelper.U_T_SCORE);
        score = c.getInt(userIndex);


        return score;

    }

    public void setTriviaScore(int score) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE " + dbHelper.U_TABLE_NAME +
                " SET " + dbHelper.U_T_SCORE + " = " + score +
                " WHERE " + dbHelper.U_isACTIVE + " = 1;");

    }

    public int getTriviaCount() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        String[] columns = {dbHelper.U_T_COUNT};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, dbHelper.U_isACTIVE + " = " + 1, null, null, null, null);

        c.moveToFirst();

        int userIndex = c.getColumnIndex(dbHelper.U_T_COUNT);
        count = c.getInt(userIndex);


        return count;

    }

    public void setTriviaCount(int count) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE " + dbHelper.U_TABLE_NAME +
                " SET " + dbHelper.U_T_COUNT + " = " + count +
                " WHERE " + dbHelper.U_isACTIVE + " = 1;");

    }

    public SimpleCursorAdapter viewUserList(Context context) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {dbHelper.U_ID, dbHelper.U_NAME};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, null, null, null, null, null);

        String[] from = {dbHelper.U_NAME};
        int[] to = {android.R.id.text1};


        SimpleCursorAdapter qListAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, c, from, to);

        return qListAdapter;
    }

    public void deleteUser(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + dbHelper.U_TABLE_NAME +
                " WHERE " + dbHelper.U_NAME + " = '" + user + "';");


    }

    public int numUsers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.U_ID, dbHelper.U_NAME};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, null, null, null, null, null);
        int num = c.getCount();
        return num;
    }

    public String getActiveUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String user;

        String[] columns = {dbHelper.U_NAME};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, dbHelper.U_isACTIVE + " = " + 1, null, null, null, null);

        c.moveToFirst();

        int userIndex = c.getColumnIndex(dbHelper.U_NAME);
        user = c.getString(userIndex);


        return user;

    }

    public void setActiveUser(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE " + dbHelper.U_TABLE_NAME +
                " SET " + dbHelper.U_isACTIVE + "= 0" +
                " WHERE " + dbHelper.U_isACTIVE + "  = 1;");

        db.execSQL("UPDATE " + dbHelper.U_TABLE_NAME +
                " SET " + dbHelper.U_isACTIVE + " = 1" +
                " WHERE " + dbHelper.U_NAME + " = '" + user + "';");


    }

    public String getTQuestion(int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Question;

        String[] columns = {dbHelper.T_QUESTION};
        Cursor c = db.query(dbHelper.T_TABLE_NAME, columns, dbHelper.T_ID + " = " + count, null, null, null, null);

        c.moveToFirst();

        int questionIndex = c.getColumnIndex(dbHelper.T_QUESTION);
        Question = c.getString(questionIndex);


        return Question;

    }

    public String getTAnswer(int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String ans;

        String[] columns = {dbHelper.T_ANSWER};
        Cursor c = db.query(dbHelper.T_TABLE_NAME, columns, dbHelper.T_ID + " = " + count, null, null, null, null);

        c.moveToFirst();

        int ansIndex = c.getColumnIndex(dbHelper.T_ANSWER);
        ans = c.getString(ansIndex);

        return ans;

    }

    public String[] getTOptions(int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] options = new String[4];
        String[] columns = {dbHelper.T_ANSWER, dbHelper.T_WRONG1, dbHelper.T_WRONG2, dbHelper.T_WRONG3};
        Cursor c = db.query(dbHelper.T_TABLE_NAME, columns, dbHelper.T_ID + " = " + count, null, null, null, null);

        c.moveToFirst();

        int indexAns, indexW1, indexW2, indexW3;
        indexAns = c.getColumnIndex(dbHelper.T_ANSWER);
        indexW1 = c.getColumnIndex(dbHelper.T_WRONG1);
        indexW2 = c.getColumnIndex(dbHelper.T_WRONG2);
        indexW3 = c.getColumnIndex(dbHelper.T_WRONG3);

        options[0] = c.getString(indexAns);
        options[1] = c.getString(indexW1);
        options[2] = c.getString(indexW2);
        options[3] = c.getString(indexW3);

        return options;
    }

    public int numQuestions() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.T_ID, dbHelper.T_QUESTION};
        Cursor c = db.query(dbHelper.T_TABLE_NAME, columns, null, null, null, null, null);
        int num = c.getCount();
        return num;
    }

    public SimpleCursorAdapter viewTriviaQuestionList(Context context) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {dbHelper.T_ID, dbHelper.T_QUESTION};
        Cursor c = db.query(dbHelper.T_TABLE_NAME, columns, null, null, null, null, null);

        String[] from = {dbHelper.T_QUESTION};
        int[] to = {android.R.id.text1};


        SimpleCursorAdapter qListAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, c, from, to);

        return qListAdapter;
    }

    public void insertQuestion(String question, String answer, String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.F_FRONT_FACE, question);
        values.put(dbHelper.F_BACK_FACE, answer);
        values.put(dbHelper.F_USER, user);
        db.insert(dbHelper.F_TABLE_NAME, null, values);
    }

    public void deleteFcard(String front) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] frontface = {front};
        db.delete(dbHelper.F_TABLE_NAME, dbHelper.F_FRONT_FACE + "=?", frontface);
    }

    public int getFlashLength(String activeUser) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.F_ID, dbHelper.F_FRONT_FACE, dbHelper.F_USER};
        Cursor c = db.query(dbHelper.F_TABLE_NAME, columns, dbHelper.F_USER + "= '" + activeUser + "'", null, null, null, null);
        int num = c.getCount();
        return num;
    }

    public String getSWord(int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Question;

        String[] columns = {dbHelper.S_ID, dbHelper.S_WORD};
        Cursor c = db.query(dbHelper.S_TABLE_NAME, columns, dbHelper.S_ID + " = " + count, null, null, null, null);

        c.moveToFirst();

        int questionIndex = c.getColumnIndex(dbHelper.S_WORD);
        Question = c.getString(questionIndex);


        return Question;

    }

    public int numWords() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.S_ID, dbHelper.S_WORD};
        Cursor c = db.query(dbHelper.S_TABLE_NAME, columns, null, null, null, null, null);
        int num = c.getCount();
        return num;
    }

    public int getSpellingCount() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        String[] columns = {dbHelper.U_S_COUNT};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, dbHelper.U_isACTIVE + " = " + 1, null, null, null, null);

        c.moveToFirst();

        int userIndex = c.getColumnIndex(dbHelper.U_S_COUNT);
        count = c.getInt(userIndex);


        return count;

    }

    public void setSpellingCount(int score) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE " + dbHelper.U_TABLE_NAME +
                " SET " + dbHelper.U_S_COUNT + " = " + score +
                " WHERE " + dbHelper.U_isACTIVE + " = 1;");


    }

    public int getSpellingScore() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        String[] columns = {dbHelper.U_S_SCORE};
        Cursor c = db.query(dbHelper.U_TABLE_NAME, columns, dbHelper.U_isACTIVE + " = " + 1, null, null, null, null);

        c.moveToFirst();

        int userIndex = c.getColumnIndex(dbHelper.U_S_SCORE);
        count = c.getInt(userIndex);


        return count;

    }

    public void setSpellingScore(int score) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE " + dbHelper.U_TABLE_NAME +
                " SET " + dbHelper.U_S_SCORE + " = " + score +
                " WHERE " + dbHelper.U_isACTIVE + " = 1;");

    }

    public SimpleCursorAdapter earnedList(Context context) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {dbHelper.A_ID, dbHelper.A_NAME, dbHelper.A_EARNED};
        Cursor c = db.query(dbHelper.A_TABLE_NAME, columns, dbHelper.A_EARNED + " = " + 1, null, null, null, null);

        String[] from = {dbHelper.A_NAME};
        int[] to = {android.R.id.text1};


        SimpleCursorAdapter ListAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, c, from, to);

        return ListAdapter;
    }

    public void setEarnedAchiv(String type, int score) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE " + dbHelper.A_TABLE_NAME +
                " SET " + dbHelper.A_EARNED + "= " + 0 +
                " WHERE " + dbHelper.A_ACH_TYPE + "  = '" + type + "';");

        db.execSQL("UPDATE " + dbHelper.A_TABLE_NAME +
                " SET " + dbHelper.A_EARNED + " = " + 1 +
                " WHERE " + dbHelper.A_ACH_TYPE + " = '" + type + "'" +
                " AND " + dbHelper.A_POINTS + " <= " + score + ";");


    }

    public String[] usersFlashCardsFront(String activeUser) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {dbHelper.F_ID, dbHelper.F_FRONT_FACE, dbHelper.F_USER};

        Cursor c = db.query(dbHelper.F_TABLE_NAME, columns, dbHelper.F_USER + "= '" + activeUser + "'", null, null, null, null);

        String[] arr = new String[c.getCount()];

        int i = 0;
        int textIndex = c.getColumnIndex(dbHelper.F_FRONT_FACE);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            arr[i] = c.getString(textIndex);
            i++;
            c.moveToNext();

        }

        return arr;

    }

    public String[] usersFlashCardsBack(String activeUser) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {dbHelper.F_ID, dbHelper.F_BACK_FACE, dbHelper.F_USER};

        Cursor c = db.query(dbHelper.F_TABLE_NAME, columns, dbHelper.F_USER + "= '" + activeUser + "'", null, null, null, null);

        String[] arr = new String[c.getCount()];

        int i = 0;
        int textIndex = c.getColumnIndex(dbHelper.F_BACK_FACE);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            arr[i] = c.getString(textIndex);
            i++;
            c.moveToNext();

        }

        return arr;

    }

    public int getAchievedLength() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.A_ID, dbHelper.A_EARNED};
        Cursor c = db.query(dbHelper.A_TABLE_NAME, columns, dbHelper.A_EARNED + " = " + 1, null, null, null, null);
        int num = c.getCount();
        return num;
    }

    static class MicoDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "micocards.db";
        private static final int DATABASE_VERSION = 1;

        //Trivia Table
        private static final String T_TABLE_NAME = "Trivia";
        private static final String T_ID = "_id";
        private static final String T_QUESTION = "question";
        private static final String T_ANSWER = "answer";
        private static final String T_WRONG1 = "wrong1";
        private static final String T_WRONG2 = "wrong2";
        private static final String T_WRONG3 = "wrong3";
        private static final String U_TABLE_NAME = "user";
        private static final String U_ID = "_id";
        private static final String U_NAME = "name";
        private static final String U_T_SCORE = "tScore";
        private static final String U_T_COUNT = "tCount";
        private static final String U_S_SCORE = "sScore";
        private static final String U_S_COUNT = "sCount";
        private static final String U_isACTIVE = "isActive";
        private static final String A_TABLE_NAME = "Achievement";
        private static final String A_ID = "_id";
        private static final String A_NAME = "name";
        private static final String A_ACH_TYPE = "type";
        private static final String A_POINTS = "points";
        private static final String A_EARNED = "earned";
        private static final String F_TABLE_NAME = "FlashCard";
        private static final String F_ID = "_id";
        private static final String F_FRONT_FACE = "question";
        private static final String F_BACK_FACE = "answer";
        private static final String F_USER = "user";
        private static final String S_TABLE_NAME = "spelling";
        private static final String S_ID = "_id";
        private static final String S_WORD = "word";
        private Context context;

        MicoDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            try {

                db.execSQL("CREATE TABLE " +
                        T_TABLE_NAME + "(" +
                        T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        T_QUESTION + " VARCHAR(300)," +
                        T_ANSWER + " VARCHAR(100)," +
                        T_WRONG1 + " VARCHAR(100)," +
                        T_WRONG2 + " VARCHAR(100)," +
                        T_WRONG3 + " VARCHAR(100));");

                db.execSQL("CREATE TABLE " +
                        U_TABLE_NAME + "(" +
                        U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        U_NAME + " VARCHAR(25)," +
                        U_T_COUNT + " INTEGER(3)," +
                        U_T_SCORE + " INTEGER(5)," +
                        U_S_COUNT + " INTEGER(5)," +
                        U_S_SCORE + " INTEGER(5)," +
                        U_isACTIVE + " BOOLEAN);");

                db.execSQL("CREATE TABLE " + F_TABLE_NAME + "(" +
                        F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        F_USER + " VARCHAR(50)," +
                        F_FRONT_FACE + " VARCHAR(200)," +
                        F_BACK_FACE + " VARCHAR(200));");

                db.execSQL("INSERT INTO " +
                        U_TABLE_NAME + " (" + U_NAME + "," + U_T_COUNT + "," + U_T_SCORE + "," + U_S_COUNT + "," + U_S_SCORE + "," + U_isACTIVE + ") "
                        + "VALUES ('Guest', '1', '0','1','0', '1'); ");


                db.execSQL("INSERT INTO " +
                        T_TABLE_NAME + " (" + T_QUESTION + "," + T_ANSWER + "," + T_WRONG1 + "," + T_WRONG2 + "," + T_WRONG3 + ") "
                        + "VALUES  ('Population of Ireland? (2013) ', ' 4.595 milion ', ' 4.695 million ', ' 5.595 million ', ' 5.259 million '),\n" +
                        " ('Population of the World? (2013) ', ' 7.125 billion ', ' 8.125 billion ', ' 7.655 billion ', ' 7.856 billion '),\n" +
                        " ('What does the word boreal mean? ', ' Cold ', ' Treeless ', ' Southern ', ' Northern '),\n" +
                        " ('What is the Smallest Contenent? ', ' Australia ', ' Africa ', ' Europe ', ' America '),\n" +
                        " ('The artist El Greco was born on which Greek island? ', ' Crete ', ' Rhodes ', ' Corfu ', ' Tinos '),\n" +
                        " ('Carpology is the study of what? ', ' Fruits and Seeds ', ' Fish ', ' Bones ', ' Rocks '),\n" +
                        " ('The Gaillard Cut is part of which canal connecting the Atlantic and Pacific Oceans? ', ' Panama Canal ', ' Seuz Canal ', ' Welland Canal ', ' Grand '),\n" +
                        " ('Native to Australia, what creature is a pardalote? ', ' Bird ', ' Fish ', ' Dog ', ' Cat '),\n" +
                        " ('What colour is the gemstone peridot? ', ' Green ', ' Blue ', ' Red ', ' Yellow '),\n" +
                        " ('Second Larget River in the World? ', ' Amazon River ', ' Nile ', ' Yangtze ', ' Yellow River '),\n" +
                        " ('Second Largest Mountain in the World? ', ' K2 ', ' Mount Everest ', ' kanchenjunga ', ' lhotse '),\n" +
                        " ('What Causes the Creation of Volcanos? ', ' Movement of Plates ', ' Weather ', ' Dense Population ', ' Evolution '),\n" +
                        " ('On the Periodic table what element is Ti? ', ' Titanium ', ' Iron ', ' Thallium ', ' Technetium '),\n" +
                        " ('On the Periodic table what element is Au? ', ' Gold ', ' Silver ', ' Argon ', '  Astatine '),\n" +
                        " ('On the Periodic table what is the first element? ', ' Hydrogen ', ' Kryptom ', ' Iron ', ' Oxygen '),\n" +
                        " ('How may elements on the Periodic table? ', ' 118 ', ' 116 ', ' 114 ', ' 120 '),\n" +
                        " ('Which planet has the tallest mountains? ', ' Mars ', ' Earth ', ' Venus ', ' Saturn '),\n" +
                        " ('The olfactory system in our body is related to what Sense? ', ' Smell ', ' Taste ', ' Feeling ', ' Hearing '),\n" +
                        " ('How many pairs of ribs do human beings have? ', ' 12 ', ' 14 ', ' 16 ', ' 10 '),\n" +
                        " ('What is the name for the transparent outer covering of the front of the eye? ', ' Cornea ', ' Iris ', ' Pupil ', ' Retina '),\n" +
                        " ('What negatively charged particles are responsible for the flow of electricity? ', ' Electrons ', ' Neutrons ', ' Protons ', ' Atoms '),\n" +
                        " ('The thickest part of the human skin is located in which general areas ', ' Palms/Soles ', ' Arms ', ' Legs ', ' Chest '),\n" +
                        " ('Which of these bones are not in the ear? ', ' Femur ', ' malleus ', ' ossicles ', ' stapes '),\n" +
                        " ('Which science Is interested with living things? ', ' Biology ', ' Physics ', ' Chemistry ', ' Engineering '),\n" +
                        " ('What German scientist discovered the X-Ray in 1895? ', ' Wilhelm Roentgen ', ' Albert Einstein ', ' Max Born ', ' Max Planck '),\n" +
                        " ('What date did America Declare its Independence? ', ' 4th July 1776 ', ' 4th July 1774 ', ' 4th July 1775 ', ' 4th July 1778 ')\n");

                db.execSQL("CREATE TABLE " +
                        S_TABLE_NAME + "(" +
                        S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        S_WORD + " VARCHAR(50));");

                db.execSQL("INSERT INTO " +
                        S_TABLE_NAME + " (" + S_WORD + ") "
                        + "VALUES ('Onomatopoeia'),"
                        + "('Telegraph'), "
                        + "('Excited'), "
                        + "('Evaluate'), "
                        + "('Discover'), "
                        + "('Envelope'), "
                        + "('Microphone'), "
                        + "('Evolve'), "
                        + "('Decorate'), "
                        + "('Democracy'), "
                        + "('Emulate'), "
                        + "('Evacuate'), "
                        + "('Authority'), "
                        + "('Austerity'), "
                        + "('Emancipate'), "
                        + "('Elevation'), "
                        + "('Personality'), "
                        + "('Ascendancy'), "
                        + "('Heuristic'), "
                        + "('Medal'), "
                        + "('Elderly'), "
                        + "('Grudge'), "
                        + "('Artistic'), "
                        + "('Creative'), "
                        + "('Depression'), "
                        + "('Following'), "
                        + "('Hollow'), "
                        + "('Yacht'), "
                        + "('Float'), "
                        + "('Jewels'), "
                        + "('Antelope'), "
                        + "('Script'), "
                        + "('Mystical'), "
                        + "('Secular'), "
                        + "('Sectarian'), "
                        + "('Amongst'), "
                        + "('Disgust'), "
                        + "('Authoritarian'), "
                        + "('Motion'), "
                        + "('Legislation'); ");

                db.execSQL("CREATE TABLE " +
                        A_TABLE_NAME + "(" +
                        A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        A_NAME + " VARCHAR(300)," +
                        A_ACH_TYPE + " VARCHAR(50)," +
                        A_POINTS + " INTEGER(10), " +
                        A_EARNED + " BOOLEAN);");


                db.execSQL("INSERT INTO " +
                        A_TABLE_NAME + " (" + A_NAME + ", " + A_ACH_TYPE + ", " + A_POINTS + ", " + A_EARNED + ") "
                        + "VALUES ('My First trivia point!', 'trivia', '1','0'), "
                        + "('5 Trivia points!', 'trivia', '5','0'), "
                        + "('10 Trivia points!', 'trivia', '10','0'), "
                        + "('15 Trivia points!', 'trivia', '15','0'), "
                        + "('20 Trivia Points', 'trivia', '20','0'), "
                        + "('25 Trivia Points', 'trivia', '25','0'), "
                        + "('30 Trivia Points', 'trivia', '30','0'), "
                        + "('35 Trivia Points', 'trivia', '35','0'), "
                        + "('40 Trivia Points', 'trivia', '40','0'), "
                        + "('My First flashcard point!', 'flash', '1','0'), "
                        + "('5 flashcard points!', 'flash', '5','0'), "
                        + "('10 flashcard points!', 'flash', '10','0'), "
                        + "('15 flashcard points!', 'flash', '15','0'), "
                        + "('20 flashcard points!', 'flash', '20','0'), "
                        + "('25 flashcard points!', 'flash', '25','0'), "
                        + "('30 flashcard points!', 'flash', '30','0'), "
                        + "('My First spelling point!', 'spelling', '1','0'), "
                        + "('5 spelling points', 'spelling', '5','0'), "
                        + "('10 spelling points', 'spelling', '10','0'), "
                        + "('15 spelling points', 'spelling', '15','0'), "
                        + "('20 spelling points', 'spelling', '20','0'), "
                        + "('25 spelling points!', 'spelling', '25','0'), "
                        + "('30 spelling points!', 'spelling', '30','0'), "
                        + "('35 spelling points!', 'spelling', '35','0'), "
                        + "('40 spelling points!', 'spelling', '40','0');");

            } catch (SQLException e) {
                e.printStackTrace();
                Message.message(context, "" + e);
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL("DROP TABLE IF EXISTS " + T_TABLE_NAME + ";");
                db.execSQL("DROP TABLE IF EXISTS " + U_TABLE_NAME + ";");
                db.execSQL("DROP TABLE IF EXISTS " + F_TABLE_NAME + ";");
                db.execSQL("DROP TABLE IF EXISTS " + S_TABLE_NAME + ";");
                db.execSQL("DROP TABLE IF EXISTS " + A_TABLE_NAME + ";");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
                Message.message(context, "" + e);
            }
        }
    }

}