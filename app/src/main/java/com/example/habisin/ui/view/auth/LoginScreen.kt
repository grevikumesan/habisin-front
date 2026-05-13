package com.example.habisin.ui.view.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.ui.components.FieldLabel
import com.example.habisin.ui.components.FilledField
import com.example.habisin.ui.components.LogoPlaceholder
import com.example.habisin.ui.theme.BrandBlue
import com.example.habisin.ui.theme.DividerGray
import com.example.habisin.ui.theme.LabelColor
import com.example.habisin.ui.uistate.LoginUiState
import com.example.habisin.ui.viewmodel.LoginViewModel
import androidx.compose.ui.res.stringResource
import com.example.habisin.R

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val state = viewModel.loginUiState
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) {
            onLoginSuccess()
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

        FieldLabel(stringResource(R.string.login))
        Spacer(Modifier.height(6.dp))
        FilledField(
            value         = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            placeholder   = stringResource(R.string.auth_enter_email),
            keyboardType  = KeyboardType.Email
        )

        Spacer(Modifier.height(20.dp))

        FieldLabel(stringResource(R.string.password))
        Spacer(Modifier.height(6.dp))
        FilledField(
            value         = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder   = stringResource(R.string.auth_password),
            keyboardType  = KeyboardType.Password,
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) stringResource(R.string.hide_password)
                        else stringResource(R.string.show_password),
                        tint = Color(0xFF555555)
                    )
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier         = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick        = onForgotPassword,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(stringResource(R.string.forgot_password), color = BrandBlue, fontWeight = FontWeight.Medium)
            }
        }

        if (state is LoginUiState.Error) {
            Spacer(Modifier.height(8.dp))
            Text(
                text  = state.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick  = viewModel::login,
            enabled  = state !is LoginUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape  = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandBlue,
                contentColor   = Color.White
            )
        ) {
            if (state is LoginUiState.Loading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color       = Color.White
                )
            } else {
                Text(stringResource(R.string.sign_in), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
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
            Text(stringResource(R.string.dont_have_an_account), color = LabelColor)
            Spacer(Modifier.width(6.dp))
            TextButton(
                onClick        = onNavigateToRegister,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(stringResource(R.string.sign_up_now), color = BrandBlue, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}