#=============================== $!{className} 模块=======================================
$!{bealowerNmae}_title=$!{className}
#foreach($field in $!fields)
$!{bealowerNmae}_$!field.name=$!field.name
#end
