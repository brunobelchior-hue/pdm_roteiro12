package br.com.entregacep.presentation;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import br.com.entregacep.domain.*;
public class EditorActivity extends ComponentActivity {
    private final EditText[] fields=new EditText[11];
    private EditorViewModel vm;
    private long id;
    @Override public void onCreate(Bundle state){
        super.onCreate(state);id=getIntent().getLongExtra("id",0);
        LinearLayout root=Ui.page(this,id==0?"Novo destino":"Editar destino","Consulte o CEP e complete os detalhes da entrega.");
        ScrollView scroll=new ScrollView(this);LinearLayout form=new LinearLayout(this);form.setOrientation(1);scroll.addView(form);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        String[] labels={"Nome do ponto *","Destinatário *","CEP *","Rua *","Número * (ou S/N)","Complemento","Bairro","Cidade *","UF *","Instruções de entrega","Referência de entrega"};
        // The final reference is combined with instructions when saved.
        vm=new ViewModelProvider(this).get(EditorViewModel.class);
        Button[] lookup={null};
        for(int i=0;i<fields.length;i++){fields[i]=Ui.field(this,form,labels[i],100+i);if(i==2){fields[i].setInputType(InputType.TYPE_CLASS_NUMBER);lookup[0]=Ui.button(this,form,"Consultar CEP na BrasilAPI",v->vm.lookup(value(2)));}}
        Button save=Ui.button(this,form,"Salvar ponto",v->{DeliveryPoint p=new DeliveryPoint();p.id=id;p.name=value(0);p.recipient=value(1);p.cep=value(2);p.street=value(3);p.number=value(4);p.complement=value(5);p.neighborhood=value(6);p.city=value(7);p.state=value(8).toUpperCase(java.util.Locale.ROOT);p.instructions=value(9)+(value(10).isEmpty()?"":" · Referência: "+value(10));vm.save(p);});
        Ui.button(this,form,"Cancelar e voltar",v->finish());
        TextView status=Ui.text(this,"",15);root.addView(status);
        vm.loaded.observe(this,p->{if(state==null){String[] values={p.name,p.recipient,p.cep,p.street,p.number,p.complement,p.neighborhood,p.city,p.state,p.instructions,""};for(int i=0;i<fields.length;i++)fields[i].setText(values[i]);}});
        vm.address.observe(this,a->{String current;try{current=DeliveryUseCases.normalizeCep(value(2));}catch(Exception e){return;}if(!a.cep.equals(current))return;fields[3].setText(a.street);fields[6].setText(a.neighborhood);fields[7].setText(a.city);fields[8].setText(a.state);status.setText("CEP consultado. Complete número e dados de entrega.");});
        vm.busy.observe(this,b->{save.setEnabled(!b);lookup[0].setEnabled(!b);for(EditText f:fields)f.setEnabled(!b);if(b)status.setText("Aguarde…");});
        vm.error.observe(this,e->{if(e!=null){status.setText(e);Toast.makeText(this,e,Toast.LENGTH_LONG).show();}});
        vm.saved.observe(this,s->{if(s){Toast.makeText(this,"Ponto salvo",Toast.LENGTH_SHORT).show();finish();}});
        if(id!=0&&vm.loaded.getValue()==null)vm.load(id);
    }
    private String value(int i){return fields[i].getText().toString().trim();}
}
