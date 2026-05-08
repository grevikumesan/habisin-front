package com.example.habisin.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.R
import com.example.habisin.ui.components.FieldLabel
import com.example.habisin.ui.components.FilledField
import com.example.habisin.ui.components.LogoPlaceholder
import com.example.habisin.ui.theme.BrandBlue
import com.example.habisin.ui.theme.DividerGray
import com.example.habisin.ui.theme.LabelColor
import com.example.habisin.ui.uistate.RegisterUiState
import com.example.habisin.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state = viewModel.registerUiState
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is RegisterUiState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LogoPlaceholder()


        FieldLabel(stringResource(R.string.username))
        Spacer(Modifier.height(6.dp))
        FilledField(
            value         = viewModel.name,
            onValueChange = viewModel::onNameChange,
            placeholder   = stringResource(R.string.enter_username)
        )

        Spacer(Modifier.height(16.dp))

        FieldLabel(stringResource(R.string.email))
        Spacer(Modifier.height(6.dp))
        FilledField(
            value         = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            placeholder   = stringResource(R.string.enter_email),
            keyboardType  = KeyboardType.Email
        )

        Spacer(Modifier.height(16.dp))

        FieldLabel("Password")
        Spacer(Modifier.height(6.dp))
        FilledField(
            value         = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder   = stringResource(R.string.enter_password),
            keyboardType  = KeyboardType.Password,
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF555555)
                    )
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        FieldLabel(stringResource(R.string.confirm_password))
        Spacer(Modifier.height(6.dp))
        FilledField(
            value         = viewModel.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            placeholder   = stringResource(R.string.re_enter_password),
            keyboardType  = KeyboardType.Password,
            visualTransformation = if (confirmVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                    Icon(
                        imageVector = if (confirmVisible) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF555555)
                    )
                }
            }
        )

        if (state is RegisterUiState.Error) {
            Spacer(Modifier.height(12.dp))
            Text(
                text  = state.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick  = viewModel::register,
            enabled  = state !is RegisterUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape  = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandBlue,
                contentColor   = Color.White
            )
        ) {
            if (state is RegisterUiState.Loading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color       = Color.White
                )
            } else {
                Text(stringResource(R.string.sign_up), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = DividerGray)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.already_have_an_account), color = LabelColor)
            Spacer(Modifier.width(6.dp))
            TextButton(
                onClick        = onNavigateToLogin,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(stringResource(R.string.log_in), color = BrandBlue, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}