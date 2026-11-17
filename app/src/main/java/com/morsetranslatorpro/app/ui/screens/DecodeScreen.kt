package com.morsetranslatorpro.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.morsetranslatorpro.app.R
import com.morsetranslatorpro.app.ui.components.ResultCard
import com.morsetranslatorpro.app.ui.viewmodel.TranslatorViewModel

/** Morse → Text screen. Accepts '.', '-', spaces and '/'. */
@Composable
fun DecodeScreen(viewModel: TranslatorViewModel, modifier: Modifier = Modifier) {
    val input by viewModel.input.collectAsStateWithLifecycle()
    val output by viewModel.output.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { viewModel.onInputChange(it, encode = false) },
            label = { Text("Enter Morse (. - / )") },
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace),
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.padding(top = 12.dp)) {
            Button(onClick = { viewModel.save(encode = false) }) { Text(stringResource(R.string.save)) }
            OutlinedButton(onClick = { viewModel.clear() }, modifier = Modifier.padding(start = 12.dp)) {
                Text(stringResource(R.string.clear))
            }
        }
        ResultCard(
            title = "Decoded text",
            content = output,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
