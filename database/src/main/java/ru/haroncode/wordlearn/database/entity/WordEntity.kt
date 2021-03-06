package ru.haroncode.wordlearn.database.entity

import androidx.room.*
import org.threeten.bp.Instant
import ru.haroncode.wordlearn.database.converter.WordExampleConverter
import ru.haroncode.wordlearn.word.model.WordExample

/**
 * @author HaronCode.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WordCategoryEntity::class,
            childColumns = ["categoryId"],
            parentColumns = ["id"]
        )
    ],
    indices = [
        Index("categoryId")
    ]
)
@TypeConverters(value = [WordExampleConverter::class])
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val wordId: Long? = null,
    val categoryId: Long,
    val word: String,
    val phraseToMemorize: String,
    val translation: String,
    val reviewCount: Int,
    val isStudy: Boolean,
    val lastReviewDate: Instant?,
    val examplesList: List<WordExample>
)
