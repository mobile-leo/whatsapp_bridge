package com.whatsappbridge.whatsapp_bridge

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity

class MainActivity : FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val action = intent?.action
        val data = intent?.data

        // Só processa se for um Intent de discagem/visualização com scheme "tel:"
        if ((action == Intent.ACTION_DIAL || action == Intent.ACTION_VIEW) &&
            data?.scheme == "tel"
        ) {
            val rawNumber = data.schemeSpecificPart ?: ""
            val whatsappNumber = buildWhatsAppNumber(rawNumber)

            if (whatsappNumber != null) {
                openWhatsApp(whatsappNumber)
            } else {
                Toast.makeText(this, "Número de telefone inválido.", Toast.LENGTH_SHORT).show()
            }

            // Encerra o app após redirecionar (ou falhar)
            finish()
        }
        // Se aberto diretamente pelo launcher, o Flutter cuida do resto (tela padrão)
    }

    /**
     * Recebe qualquer string de número, remove tudo que não for dígito ou '+',
     * e garante que começa com o código do país (55 para Brasil, se ausente).
     */
    private fun buildWhatsAppNumber(raw: String): String? {
        if (raw.isBlank()) return null

        // Remove caracteres de formatação: espaços, hífens, parênteses, pontos
        val digitsOnly = raw.replace(Regex("[^\\d+]"), "")

        if (digitsOnly.isEmpty()) return null

        return when {
            // Já começa com '+' — remove o '+', mantém o restante
            digitsOnly.startsWith("+") -> digitsOnly.removePrefix("+")

            // Começa com '55' seguido de DDD (2 dígitos) + número (8-9 dígitos) = 13 dígitos
            digitsOnly.startsWith("55") && digitsOnly.length >= 12 -> digitsOnly

            // Número local brasileiro: 10 ou 11 dígitos (DDD + número) → adiciona 55
            digitsOnly.length == 10 || digitsOnly.length == 11 -> "55$digitsOnly"

            // Qualquer outro caso: adiciona 55 e tenta assim mesmo
            else -> "55$digitsOnly"
        }
    }

    /**
     * Abre o WhatsApp com o número formatado via deep link oficial.
     * Tenta primeiro o WhatsApp comum, depois o Business, e por fim o link web.
     */
    private fun openWhatsApp(number: String) {
        val whatsappUrl = "https://api.whatsapp.com/send?phone=$number"
        val whatsappDeepLink = "whatsapp://send?phone=$number"

        val deepLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappDeepLink)).apply {
            setPackage("com.whatsapp")
        }

        val businessIntent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappDeepLink)).apply {
            setPackage("com.whatsapp.w4b")
        }

        val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))

        when {
            deepLinkIntent.resolveActivity(packageManager) != null -> {
                startActivity(deepLinkIntent)
            }
            businessIntent.resolveActivity(packageManager) != null -> {
                startActivity(businessIntent)
            }
            else -> {
                // Fallback: abre no navegador o link do WhatsApp Web
                startActivity(fallbackIntent)
            }
        }
    }
}
