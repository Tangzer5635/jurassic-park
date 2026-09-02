import React, {useEffect, useMemo, useState} from 'react'
import {createRoot} from 'react-dom/client'
import {Activity, AlertTriangle, CheckCircle2, ChevronDown, CirclePlus, ClipboardList, Edit3, LayoutDashboard, Menu, PawPrint, Plus, RefreshCw, Search, Shield, Trash2, Users, X, MapPinned} from 'lucide-react'
import './styles.css'

const API='/api/v1'
const labels={
  EN_BONNE_SANTE:'En bonne santé',BLESSE:'Blessé',MALADE:'Malade',EN_QUARANTAINE:'En quarantaine',DECEDE:'Décédé',
  PLANIFIEE:'Planifiée',EN_COURS:'En cours',TERMINEE:'Terminée',ANNULEE:'Annulée',
  CARNIVORE:'Carnivore',HERBIVORE:'Herbivore',OMNIVORE:'Omnivore',FAIBLE:'Faible',MODERE:'Modéré',ELEVE:'Élevé',CRITIQUE:'Critique',
  AQUATIQUE:'Aquatique',TERRESTRE:'Terrestre',VOLIERE:'Volière',QUARANTAINE:'Quarantaine',CIMETIERE:'Cimetière',
  STANDARD:'Standard',RENFORCE:'Renforcé',MAXIMUM:'Maximum',JUNIOR:'Junior',CONFIRME:'Confirmé',EXPERT:'Expert',ELITE:'Élite',
  MALE:'Mâle',FEMELLE:'Femelle',INCONNU:'Inconnu',
  NOURRISSAGE:'Nourrissage',NETTOYAGE:'Nettoyage',SURVEILLANCE:'Surveillance',DEPLACEMENT:'Déplacement',EQUARRISSAGE:'Équarrissage',SOIN_MEDICAL:'Soin médical',CAPTURE_URGENTE:'Capture urgente',
  ACTIF:'Actif',MAINTENANCE:'Maintenance',FERME:'Fermé',EVACUE:'Évacué',VOLANT:'Volant'
}
const t=v=>labels[v]||v||'—'
const cls=v=>String(v||'').toLowerCase().replaceAll('_','-')
async function api(path, options={}){const r=await fetch(API+path,{...options,headers:{'Content-Type':'application/json',...(options.headers||{})}});if(r.status===204)return null;let d=null;try{d=await r.json()}catch{}if(!r.ok)throw Error(d?.message||d?.error||`Erreur ${r.status}`);return d}
function fmtDate(v){if(!v)return '—';const d=new Date(v);return isNaN(d)?v:d.toLocaleString('fr-FR',{day:'2-digit',month:'2-digit',year:'numeric',hour:'2-digit',minute:'2-digit'})}
function Badge({value}){return <span className={`badge ${cls(value)}`}>{t(value)}</span>}

function MultiSelect({label,items,value,onChange,renderLabel,placeholder='Sélectionner…'}){
  const [open,setOpen]=useState(false)
  const selected=items.filter(x=>value.includes(x.id))
  const toggle=id=>onChange(value.includes(id)?value.filter(x=>x!==id):[...value,id])
  return <div className="field full">
    <label>{label}</label>
    <div className="multi" onClick={()=>setOpen(!open)}>
      <div className="selected-chips">
        {selected.length===0?<span className="placeholder">{placeholder}</span>:selected.map(x=><span className="chip" key={x.id}>{renderLabel(x)}<button type="button" onClick={e=>{e.stopPropagation();toggle(x.id)}}><X size={12}/></button></span>)}
      </div><ChevronDown size={16}/>
    </div>
    {open&&<div className="multi-menu">
      <div className="multi-title">{selected.length} sélectionné(s)</div>
      {items.map(x=><button type="button" className={`multi-option ${value.includes(x.id)?'selected':''}`} key={x.id} onClick={()=>toggle(x.id)}><span className="check">{value.includes(x.id)?'✓':''}</span>{renderLabel(x)}</button>)}
      {items.length===0&&<div className="empty-option">Aucune donnée disponible</div>}
    </div>}
    <span className="hint">Tu peux sélectionner plusieurs éléments.</span>
  </div>
}

const resourceMap={especes:'especes',animaux:'animaux',enclos:'enclos',soigneurs:'personnels',interventions:'interventions'}
function App(){
  const [view,setView]=useState('dashboard'),[menu,setMenu]=useState(false),[refresh,setRefresh]=useState(0),[toast,setToast]=useState(''),[modal,setModal]=useState(null)
  const nav=[['dashboard','Tableau de bord',LayoutDashboard],['especes','Espèces',PawPrint],['animaux','Animaux',PawPrint],['enclos','Zones / Enclos',MapPinned],['soigneurs','Soigneurs',Users],['interventions','Interventions',ClipboardList]]
  const titles=Object.fromEntries(nav.map(x=>[x[0],x[1]]))
  const notify=m=>{setToast(m);setTimeout(()=>setToast(''),3000)}
  return <div className="app"><aside className={menu?'sidebar open':'sidebar'}><div className="brand"><div className="logo">JP</div><div><strong>Jurassic Park</strong><small>Centre de contrôle</small></div></div><nav>{nav.map(([id,label,Icon])=><button key={id} className={view===id?'nav active':'nav'} onClick={()=>{setView(id);setMenu(false)}}><Icon size={17}/>{label}</button>)}</nav><div className="side-status"><i/>Système opérationnel</div></aside><main><header><button className="mobile" onClick={()=>setMenu(!menu)}><Menu/></button><div><small>PARC · ADMINISTRATION</small><h1>{titles[view]}</h1></div><div className="head-actions"><span className="api-live"><i/> API connectée</span><button className="round" onClick={()=>setRefresh(x=>x+1)}><RefreshCw size={16}/></button></div></header><div className="content"><View view={view} refresh={refresh} openModal={setModal} notify={notify}/></div></main>{toast&&<div className="toast"><CheckCircle2 size={16}/>{toast}</div>}{modal&&<Modal {...modal} close={()=>setModal(null)} notify={notify} refresh={()=>setRefresh(x=>x+1)}/>}</div>
}

function View({view,openModal,notify,refresh}){
  const [data,setData]=useState([]),[loading,setLoading]=useState(false),[search,setSearch]=useState(''),[filter,setFilter]=useState(''),[refreshTick,setRefreshTick]=useState(0)
  useEffect(()=>{let alive=true;if(view==='dashboard')return;setLoading(true);api(`/${resourceMap[view]}/?page=1&size=100`).then(d=>{if(alive)setData(d.content||[])}).catch(e=>notify(e.message)).finally(()=>alive&&setLoading(false));return()=>alive=false},[view,refresh,refreshTick])
  if(view==='dashboard')return <Dashboard refresh={refresh} openModal={openModal}/>
  const filtered=data.filter(x=>JSON.stringify(x).toLowerCase().includes(search.toLowerCase())).filter(x=>!filter||x.etat===filter||x.etatSante===filter||x.type===filter||x.niveauHabilitation===filter)
  const create=()=>openModal({kind:{especes:'espece',animaux:'animal',enclos:'enclos',soigneurs:'personnel',interventions:'intervention'}[view],data:null})
  const remove=async x=>{if(!confirm(`Supprimer « ${x.code||x.prenom||x.nom} » ?`))return;try{await api(`/${resourceMap[view]}/${x.id}/`,{method:'DELETE'});notify('Suppression effectuée.');window.location.reload()}catch(e){notify(e.message)}}
  return <section><div className="section-head"><div><small>GESTION</small><h2>{view==='especes'?'Gestion des espèces':view==='animaux'?'Gestion des animaux':view==='enclos'?'Gestion des zones / enclos':view==='soigneurs'?'Gestion des soigneurs':'Gestion des interventions'}</h2><p>Gérez les données du parc depuis une interface unique.</p></div><button className="primary" onClick={create}><Plus size={17}/> {view==='interventions'?'Planifier une intervention':'Nouveau'}</button></div><div className="toolbar"><div className="search"><Search size={16}/><input value={search} onChange={e=>setSearch(e.target.value)} placeholder="Rechercher…"/></div>{view==='animaux'&&<Filter value={filter} set={setFilter} opts={['EN_BONNE_SANTE','BLESSE','MALADE','EN_QUARANTAINE','DECEDE']}/>} {view==='interventions'&&<Filter value={filter} set={setFilter} opts={['PLANIFIEE','EN_COURS','TERMINEE','ANNULEE']}/>} {view==='soigneurs'&&<Filter value={filter} set={setFilter} opts={['JUNIOR','CONFIRME','EXPERT','ELITE']}/>}<span className="count">{filtered.length} résultat(s)</span></div><div className="panel table-wrap">{loading?<div className="loading">Chargement…</div>:<Table view={view} rows={filtered} openModal={openModal} remove={remove} notify={notify} refreshData={()=>setRefreshTick(x=>x+1)}/>}</div></section>
}
function Filter({value,set,opts}){return <select value={value} onChange={e=>set(e.target.value)}><option value="">Tous les états</option>{opts.map(x=><option key={x} value={x}>{t(x)}</option>)}</select>}
const interventionTransitions={PLANIFIEE:['EN_COURS','ANNULEE'],EN_COURS:['TERMINEE','ANNULEE'],TERMINEE:[],ANNULEE:[]}
async function changeInterventionStatus(x,next,notify,refresh){
  if(!interventionTransitions[x.etat]?.includes(next)) return
  try{
    await api(`/interventions/${x.id}/`,{method:'PUT',body:JSON.stringify({
      id:x.id,code:x.code,dateDebut:x.dateDebut,dateFin:x.dateFin,etat:next,type:x.type,
      animalId:(x.animals||[]).map(a=>a.id),personnelId:(x.personnels||[]).map(p=>p.id),enclosId:x.enclos?.id||null
    })})
    notify(`Intervention ${x.code} : ${t(next)}.`)
    refresh()
  }catch(e){notify(`Transition refusée : ${e.message}`)}
}
function InterventionStatusActions({item,notify,refresh}){
  const next=interventionTransitions[item.etat]||[]
  if(!next.length) return <div className="status-locked"><Badge value={item.etat}/><span>{item.etat==='TERMINEE'?'Clôturée':'Annulée'}</span></div>
  return <div className="status-actions">
    <Badge value={item.etat}/>
    {next.map(n=><button key={n} className={`status-btn ${n==='ANNULEE'?'cancel':''}`} onClick={()=>changeInterventionStatus(item,n,notify,refresh)}>
      {n==='EN_COURS'?'▶':n==='TERMINEE'?'✓':'×'} {t(n)}
    </button>)}
  </div>
}

function Table({view,rows,openModal,remove,notify,refreshData}){if(!rows.length)return <div className="loading">Aucune donnée trouvée.</div>;const action=x=><div className="actions"><button onClick={()=>openModal({kind:({especes:'espece',animaux:'animal',enclos:'enclos',soigneurs:'personnel',interventions:'intervention'}[view]),data:x})}><Edit3 size={14}/></button><button className="danger" onClick={()=>remove(x)}><Trash2 size={14}/></button></div>
 if(view==='interventions')return <table><thead><tr><th>Code</th><th>Type</th><th>Début</th><th>Fin</th><th>Animaux</th><th>Personnel</th><th>État</th><th/></tr></thead><tbody>{rows.map(x=><tr key={x.id}><td className="code">{x.code}</td><td><Badge value={x.type}/></td><td>{fmtDate(x.dateDebut)}</td><td>{fmtDate(x.dateFin)}</td><td><div className="stack">{(x.animals||[]).map(a=><span key={a.id}>{a.prenom} <em>{a.code}</em></span>)}</div></td><td><div className="stack">{(x.personnels||[]).map(p=><span key={p.id}>{p.prenom} {p.nom}</span>)}</div></td><td><InterventionStatusActions item={x} notify={notify} refresh={refreshData}/></td><td>{action(x)}</td></tr>)}</tbody></table>
 if(view==='especes')return <table><thead><tr><th>Code</th><th>Espèce</th><th>Type</th><th>Alimentation</th><th>Dangerosité</th><th/></tr></thead><tbody>{rows.map(x=><tr key={x.id}><td className="code">{x.code}</td><td><b>{x.nom}</b></td><td><Badge value={x.type}/></td><td>{t(x.alimentation)}</td><td><Badge value={x.dangerosite}/></td><td>{action(x)}</td></tr>)}</tbody></table>
 if(view==='animaux')return <table><thead><tr><th>Code</th><th>Animal</th><th>Espèce</th><th>Enclos</th><th>Sexe</th><th>Santé</th><th/></tr></thead><tbody>{rows.map(x=><tr key={x.id}><td className="code">{x.code}</td><td><b>{x.prenom}</b></td><td>{x.espece?.nom||'—'}</td><td>{x.enclos?.code||'—'}</td><td>{t(x.sexe)}</td><td><Badge value={x.etatSante}/></td><td>{action(x)}</td></tr>)}</tbody></table>
 if(view==='enclos')return <table><thead><tr><th>Code</th><th>Type</th><th>Capacité</th><th>Sécurité</th><th>État</th><th/></tr></thead><tbody>{rows.map(x=><tr key={x.id}><td className="code">{x.code}</td><td><Badge value={x.type}/></td><td>{x.capaciteMax}</td><td><Badge value={x.niveauSecurite}/></td><td><Badge value={x.etat}/></td><td>{action(x)}</td></tr>)}</tbody></table>
 return <table><thead><tr><th>Code</th><th>Soigneur</th><th>Habilitation</th><th/></tr></thead><tbody>{rows.map(x=><tr key={x.id}><td className="code">{x.code}</td><td><b>{x.prenom} {x.nom}</b></td><td><Badge value={x.niveauHabilitation}/></td><td>{action(x)}</td></tr>)}</tbody></table>
}

function Dashboard({refresh,openModal}){const [stats,setStats]=useState({});useEffect(()=>{Promise.all(Object.entries({especes:'especes',animaux:'animaux',enclos:'enclos',soigneurs:'personnels',interventions:'interventions'}).map(async([k,r])=>[k,(await api(`/${r}/?page=1&size=1`)).totalElements||0])).then(x=>setStats(Object.fromEntries(x))).catch(()=>{})},[refresh]);return <><div className="hero"><div><small>VUE D'ENSEMBLE</small><h2>Bienvenue au centre de contrôle.</h2><p>Surveillez les espèces, les animaux, les zones sécurisées, les soigneurs et les interventions.</p></div><span><Shield size={14}/> Sécurité prioritaire</span></div><div className="stats">{[['Espèces','especes',PawPrint],['Animaux','animaux',PawPrint],['Zones','enclos',MapPinned],['Soigneurs','soigneurs',Users],['Interventions','interventions',ClipboardList]].map(([l,k,I])=><div className="stat" key={k}><small><I size={14}/>{l}</small><strong>{stats[k]??'—'}</strong></div>)}</div><div className="dashboard-note"><Activity size={18}/><div><b>Centre de contrôle des opérations</b><p>Une intervention peut maintenant associer autant d'animaux et de soigneurs que nécessaire, avec sélection multiple et affichage en liste.</p></div></div></>}

function Modal({kind,data,close,notify,refresh}){const isInter=kind==='intervention';const [loading,setLoading]=useState(false);const [refs,setRefs]=useState({especes:[],animaux:[],enclos:[],personnels:[]});useEffect(()=>{Promise.all(['especes','animaux','enclos','personnels'].map(r=>api(`/${r}/?page=1&size=100`).then(d=>[r,d.content||[]]))).then(x=>setRefs(Object.fromEntries(x))).catch(e=>notify(e.message))},[]);const [form,setForm]=useState(()=>initial(kind,data));const set=(k,v)=>setForm(f=>({...f,[k]:v}));const submit=async e=>{e.preventDefault();setLoading(true);let body={...form};if(isInter){body.dateDebut=body.dateDebut?new Date(body.dateDebut).toISOString().slice(0,19):null;body.dateFin=body.dateFin?new Date(body.dateFin).toISOString().slice(0,19):null;body.animalId=form.animalId;body.personnelId=form.personnelId;body.enclosId=form.enclosId?Number(form.enclosId):null}else{if(kind==='animal'){body.especeId=Number(body.especeId);body.enclosId=body.enclosId?Number(body.enclosId):null}if(kind==='enclos')body.capaciteMax=Number(body.capaciteMax)}try{const path={espece:'especes',animal:'animaux',enclos:'enclos',personnel:'personnels',intervention:'interventions'}[kind];await api(`/${path}/${data?data.id+'/':''}`,{method:data?'PUT':'POST',body:JSON.stringify(body)});notify(data?'Modification enregistrée.':'Création enregistrée.');close();refresh()}catch(e){notify(e.message)}finally{setLoading(false)}};return <div className="overlay" onMouseDown={e=>e.target===e.currentTarget&&close()}><div className="modal"><div className="modal-head"><div><small>{data?'MODIFICATION':'NOUVEL ENREGISTREMENT'}</small><h3>{isInter?'Nouvelle intervention':data?'Modifier':'Créer'} {kind==='personnel'?'un soigneur':kind==='enclos'?'un enclos':kind==='animal'?'un animal':kind==='espece'?'une espèce':''}</h3></div><button className="round" onClick={close}><X/></button></div><form onSubmit={submit}><div className="form-grid">{fields(kind,form,set,refs)}</div><div className="form-actions"><button type="button" className="secondary" onClick={close}>Annuler</button><button className="primary" disabled={loading}>{loading?'Enregistrement…':'Enregistrer'}</button></div></form></div></div>}
function initial(kind,x){if(kind==='intervention')return {id:x?.id,code:x?.code||'',type:x?.type||'SURVEILLANCE',dateDebut:x?.dateDebut?.slice(0,16)||'',dateFin:x?.dateFin?.slice(0,16)||'',etat:x?.etat||'PLANIFIEE',enclosId:x?.enclos?.id||'',animalId:(x?.animals||[]).map(a=>a.id),personnelId:(x?.personnels||[]).map(p=>p.id)};return {...(x||{})}}
function Input({label,value,onChange,type='text',required=true}){return <div className="field"><label>{label}</label><input type={type} value={value??''} onChange={e=>onChange(e.target.value)} required={required}/></div>}
function Select({label,value,onChange,opts,required=true}){return <div className="field"><label>{label}</label><select value={value??''} onChange={e=>onChange(e.target.value)} required={required}>{opts.map(o=><option key={o} value={o}>{t(o)}</option>)}</select></div>}
function fields(kind,f,set,r){if(kind==='intervention')return <><Input label="Code" value={f.code} onChange={v=>set('code',v)}/><Select label="Type" value={f.type} onChange={v=>set('type',v)} opts={['NOURRISSAGE','NETTOYAGE','SURVEILLANCE','DEPLACEMENT','EQUARRISSAGE','SOIN_MEDICAL','CAPTURE_URGENTE']}/><Input label="Début" type="datetime-local" value={f.dateDebut} onChange={v=>set('dateDebut',v)}/><Input label="Fin" type="datetime-local" value={f.dateFin} onChange={v=>set('dateFin',v)} required={false}/><Select label="État" value={f.etat} onChange={v=>set('etat',v)} opts={['PLANIFIEE','EN_COURS','TERMINEE','ANNULEE']}/><div className="field"><label>Enclos</label><select value={f.enclosId||''} onChange={e=>set('enclosId',e.target.value)}><option value="">— Aucun —</option>{r.enclos.map(x=><option key={x.id} value={x.id}>{x.code}</option>)}</select></div><MultiSelect label="Animaux concernés" items={r.animaux} value={f.animalId} onChange={v=>set('animalId',v)} renderLabel={x=><>{x.prenom} <em>{x.code}</em></>}/><MultiSelect label="Soigneurs affectés" items={r.personnels} value={f.personnelId} onChange={v=>set('personnelId',v)} renderLabel={x=><>{x.prenom} {x.nom} <em>{x.code}</em></>}/><div className="hint full">Les champs <b>Animaux concernés</b> et <b>Soigneurs affectés</b> acceptent plusieurs sélections. Le backend applique les règles métier (habilitations, disponibilité, compatibilité, etc.).</div></>;
 if(kind==='espece')return <><Input label="Code" value={f.code} onChange={v=>set('code',v)}/><Input label="Nom" value={f.nom} onChange={v=>set('nom',v)}/><Select label="Type" value={f.type} onChange={v=>set('type',v)} opts={['AQUATIQUE','TERRESTRE','VOLANT']}/><Select label="Alimentation" value={f.alimentation} onChange={v=>set('alimentation',v)} opts={['CARNIVORE','HERBIVORE','OMNIVORE']}/><Select label="Dangerosité" value={f.dangerosite} onChange={v=>set('dangerosite',v)} opts={['FAIBLE','MODERE','ELEVE','CRITIQUE']}/></>;
 if(kind==='animal')return <><Input label="Code" value={f.code} onChange={v=>set('code',v)}/><Input label="Prénom" value={f.prenom} onChange={v=>set('prenom',v)}/><Select label="Sexe" value={f.sexe} onChange={v=>set('sexe',v)} opts={['MALE','FEMELLE','INCONNU']}/><Select label="Santé" value={f.etatSante} onChange={v=>set('etatSante',v)} opts={['EN_BONNE_SANTE','BLESSE','MALADE','EN_QUARANTAINE','DECEDE']}/><div className="field"><label>Espèce</label><select value={f.especeId||f.espece?.id||''} onChange={e=>set('especeId',e.target.value)} required><option value="">— Sélectionner —</option>{r.especes.map(x=><option key={x.id} value={x.id}>{x.code} · {x.nom}</option>)}</select></div><div className="field"><label>Enclos</label><select value={f.enclosId||f.enclos?.id||''} onChange={e=>set('enclosId',e.target.value)}><option value="">— Aucun —</option>{r.enclos.map(x=><option key={x.id} value={x.id}>{x.code}</option>)}</select></div></>;
 if(kind==='enclos')return <><Input label="Code" value={f.code} onChange={v=>set('code',v)}/><Input label="Capacité maximale" type="number" value={f.capaciteMax} onChange={v=>set('capaciteMax',v)}/><Select label="Type" value={f.type} onChange={v=>set('type',v)} opts={['AQUATIQUE','TERRESTRE','VOLIERE','QUARANTAINE','CIMETIERE']}/><Select label="Sécurité" value={f.niveauSecurite} onChange={v=>set('niveauSecurite',v)} opts={['STANDARD','RENFORCE','MAXIMUM']}/><Select label="État" value={f.etat} onChange={v=>set('etat',v)} opts={['ACTIF','MAINTENANCE','FERME','EVACUE']}/></>;
 return <><Input label="Code" value={f.code} onChange={v=>set('code',v)}/><Input label="Prénom" value={f.prenom} onChange={v=>set('prenom',v)}/><Input label="Nom" value={f.nom} onChange={v=>set('nom',v)}/><Select label="Habilitation" value={f.niveauHabilitation} onChange={v=>set('niveauHabilitation',v)} opts={['JUNIOR','CONFIRME','EXPERT','ELITE']}/></>}
createRoot(document.getElementById('root')).render(<App/>)
