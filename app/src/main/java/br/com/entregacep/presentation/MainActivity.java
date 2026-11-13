package br.com.entregacep.presentation;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import br.com.entregacep.domain.DeliveryPoint;
public class MainActivity extends ComponentActivity {
    private DeliveryViewModel vm;
    private boolean trash;
    private LinearLayout list;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);trash=getIntent().getBooleanExtra("trash",false);
        LinearLayout root=Ui.page(this,trash?"Lixeira":"Pontos de entrega",trash?"Registros preservados. Restaure quando precisar.":"Organize endereços e instruções para cada destino.");
        Ui.button(this,root,trash?"Voltar aos pontos":"+ Novo ponto de entrega",v->{if(trash)finish();else startActivity(new Intent(this,EditorActivity.class));});
        if(!trash)Ui.button(this,root,"Abrir lixeira",v->startActivity(new Intent(this,MainActivity.class).putExtra("trash",true)));
        ScrollView scroll=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(1);scroll.addView(list);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        vm=new ViewModelProvider(this).get(DeliveryViewModel.class);
        vm.points.observe(this,points->{list.removeAllViews();if(points.isEmpty())list.addView(Ui.text(this,trash?"A lixeira está vazia.":"Nenhum ponto cadastrado. Comece consultando um CEP.",18));for(DeliveryPoint p:points){
            LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setPadding(16,20,16,20);list.addView(card);
            card.addView(Ui.text(this,p.name,22));card.addView(Ui.text(this,p.recipient+"\n"+p.street+", "+p.number+" "+p.complement+"\n"+p.neighborhood+" · "+p.city+"/"+p.state+"\nCEP "+p.cep,16));
            if(!p.instructions.isEmpty())card.addView(Ui.text(this,"Entrega: "+p.instructions,15));
            if(trash)Ui.button(this,card,"Restaurar ponto",v->vm.toggle(p.id,true,true));
            else {Ui.button(this,card,"Editar",v->startActivity(new Intent(this,EditorActivity.class).putExtra("id",p.id)));Ui.button(this,card,"Mover para lixeira",v->new AlertDialog.Builder(this).setTitle("Excluir "+p.name+"?").setMessage("O ponto sairá da lista e poderá ser restaurado na lixeira.").setNegativeButton("Cancelar",null).setPositiveButton("Mover",(d,w)->vm.toggle(p.id,false,false)).show());}
        }});
        vm.error.observe(this,message->{if(message!=null)Toast.makeText(this,message,Toast.LENGTH_LONG).show();});
    }
    @Override protected void onResume(){super.onResume();if(vm!=null)vm.refresh(trash);}
}
