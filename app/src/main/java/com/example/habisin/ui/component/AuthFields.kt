package com.example.habisin.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habisin.ui.theme.FieldBg
import com.example.habisin.ui.theme.FieldHint
import com.example.habisin.ui.theme.LabelColor

@Composable
fun FieldLabel(text: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text       = text,
            color      = LabelColor,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    TextField(
        value                = value,
        onValueChange        = onValueChange,
        placeholder          = { Text(placeholder, color = FieldHint) },
        singleLine           = true,
        visualTransformation = visualTransformation,
        keyboardOptions      = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon         = trailingIcon,
        shape                = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = FieldBg,
            unfocusedContainerColor = FieldBg,
            disabledContainerColor  = FieldBg,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor  = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    )
}