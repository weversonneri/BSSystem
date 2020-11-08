package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.model.Servico;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Currency;
import java.util.Locale;

import me.abhinay.input.CurrencyEditText;


public class CadastroServicoActivity extends AppCompatActivity implements View.OnClickListener {

    Servico servico;

    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference refServico;
    private boolean useCurrencySymbolAsHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_servico);

        firebaseDatabase = FirebaseDatabase.getInstance();
        refServico = firebaseDatabase.getReference("servicos");

        this.mViewHolder.editCodigo = findViewById(R.id.edit_cadastro_servico_codigo);
        this.mViewHolder.editDescricao = findViewById(R.id.edit_cadastro_servico_descricao);
        this.mViewHolder.editTipo = findViewById(R.id.edit_cadastro_servico_tipo);
        this.mViewHolder.editValor = findViewById(R.id.edit_cadastro_servico_valor);
        this.mViewHolder.editObservacao = findViewById(R.id.edit_cadastro_servico_observacoes);

        this.mViewHolder.buttonCadastro = findViewById(R.id.button_cadastro_servico_confirm);
        this.mViewHolder.buttonCancel = findViewById(R.id.button_cadastro_servico_cancel);

        this.mViewHolder.buttonCancel.setOnClickListener(this);
        this.mViewHolder.buttonCadastro.setOnClickListener(this);

        this.mViewHolder.editValor = (CurrencyEditText) findViewById(R.id.edit_cadastro_servico_valor);
        this.mViewHolder.editValor.setCurrency("R$");

        this.mViewHolder.editValor.setDelimiter(false);
        this.mViewHolder.editValor.setSpacing(false);
        this.mViewHolder.editValor.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        this.mViewHolder.editValor.setSeparator(".");


        Intent intent = getIntent();
        servico = (Servico) intent.getSerializableExtra("servico");
        if (servico != null) {

            this.mViewHolder.editCodigo.setText(servico.getCodigo().toString());
            this.mViewHolder.editDescricao.setText(servico.getDescricao());
            this.mViewHolder.editTipo.setText(servico.getTipo());
            this.mViewHolder.editValor.setText(servico.getValor().toString());
            this.mViewHolder.editObservacao.setText(servico.getObservacao());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_cadastro_servico_cancel) {
            finish();
        }
        if (id == R.id.button_cadastro_servico_confirm) {
            cadastrar();
        }
    }

    private void cadastrar() {

        servico = new Servico();

        servico.setCodigo(Integer.valueOf(this.mViewHolder.editCodigo.getText().toString()));
        servico.setDescricao(this.mViewHolder.editDescricao.getText().toString());
        servico.setTipo(this.mViewHolder.editTipo.getText().toString());
        servico.setValor(Double.parseDouble(this.mViewHolder.editValor.getText().toString()));
        servico.setObservacao(this.mViewHolder.editObservacao.getText().toString());

        refServico.child(String.valueOf(servico.getCodigo())).setValue(servico);

        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        finish();

    }

    public static class ViewHolder {
        MaterialButton buttonCadastro;
        MaterialButton buttonCancel;
        TextInputEditText editCodigo, editDescricao, editTipo, editObservacao;
        CurrencyEditText  editValor;
    }
}