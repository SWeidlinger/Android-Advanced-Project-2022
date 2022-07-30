package at.fhooe.mc.ada.features.feature_budget_tracker.domain.util

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class BudgetDateMask : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return makeExpirationFilter(text)
    }

    private fun makeExpirationFilter(text: AnnotatedString): TransformedText {
        // format: XX/XX
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
            if (i == 3) out += "/"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 9
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset - 1
                if (offset <= 8) return offset - 2
                return 9
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}