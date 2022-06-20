package agis.guillaume.cleancode.datastore

import agis.guillaume.cleancode.datastore.article.ArticleDao
import agis.guillaume.cleancode.datastore.article.ArticleEntity
import agis.guillaume.cleancode.datastore.article.DateConverter
import agis.guillaume.cleancode.datastore.post.PostDao
import agis.guillaume.cleancode.datastore.post.PostEntity
import agis.guillaume.cleancode.datastore.post.UserTypeConverter
import agis.guillaume.cleancode.datastore.user.UserDao
import agis.guillaume.cleancode.datastore.user.UserEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [PostEntity::class, UserEntity::class, ArticleEntity::class], // entities stored in Room
    version = 1,
    exportSchema = false
)
@TypeConverters(UserTypeConverter::class, DateConverter::class)
abstract class MyAppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    abstract fun userDao(): UserDao

    abstract fun articleDao(): ArticleDao
}