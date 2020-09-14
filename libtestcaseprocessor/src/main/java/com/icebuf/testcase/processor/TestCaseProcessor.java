package com.icebuf.testcase.processor;

import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.icebuf.testcase.DefaultValue;
import com.icebuf.testcase.Item;
import com.icebuf.testcase.ItemEntity;
import com.icebuf.testcase.ItemGroup;
import com.icebuf.testcase.ItemType;
import com.icebuf.testcase.ResItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@AutoService(Processor.class)  //auto-service
@SupportedSourceVersion(SourceVersion.RELEASE_8)  //源码类型 1.8
public class TestCaseProcessor extends AbstractProcessor {

    private static final String OUTPUT_FILE_NAME = "testcase.json";

    private Messager messager; //使用日志打印

    private Filer filer;  //用于文件处理

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(ItemGroup.class.getName());
        types.add(Item.class.getName());
        types.add(ResItem.class.getName());
        return types;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //日志打印,在java环境下不能使用android.util.log.e()
        this.messager = processingEnvironment.getMessager();
        //文件处理工具
        this.filer = processingEnvironment.getFiler();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<String, List<ItemEntity>> listMap = new HashMap<>();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Item.class);
        for (Element element : elements) {
            handleElement(element, listMap);
        }
        Set<? extends Element> resElements = roundEnvironment.getElementsAnnotatedWith(ResItem.class);
        for (Element element : resElements) {
            handleElement(element, listMap);
        }
        for (Map.Entry<String, List<ItemEntity>> entry : listMap.entrySet()) {
            writeAndroidAssets(entry.getKey(), JSONObject.toJSONString(entry.getValue()));
        }
        return true;
    }

    private void writeAndroidAssets(String fileName, String content) {
        FileOutputStream fos = null;
        FileWriter writer = null;
        try {
            if(fileName.toLowerCase().lastIndexOf(".json") < 0) {
                fileName += ".json";
            }
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", fileName);
            String resourcePath = resource.toUri().getPath();
            messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath: " + resourcePath);
            //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
            //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
            String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
            String assetsPath = appPath + "src/main/assets";
            File dir = new File(assetsPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //写入文件
            File outputFile = new File(dir, fileName);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            if(!outputFile.createNewFile()) {
                throw new IOException("create file failed!");
            }
            messager.printMessage(Diagnostic.Kind.NOTE,
                    "write file: " + outputFile.getPath() + " content: " + content);
            writer = new FileWriter(outputFile);
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            IOUtil.close(writer, fos);
        }
    }

    private void handleElement(Element element, Map<String, List<ItemEntity>> itemsMap) {
        if(element instanceof TypeElement) {
            TypeElement typeElement = (TypeElement) element;
            String group = getTestGroup(typeElement);
            ItemEntity entity = getItemEntity(typeElement);
            if(entity == null) {
                return;
            }
            List<ItemEntity> entities = itemsMap.computeIfAbsent(group, (k)-> new ArrayList<>());
            entities.add(entity);
        }
    }

    private ItemEntity getItemEntity(TypeElement element) {
        Item item = element.getAnnotation(Item.class);
        ItemEntity entity = new ItemEntity();
        if(item != null) {
            entity.setType(ItemType.DEFAULT);
            entity.setName(item.name());
            entity.setDescription(item.description());
            entity.setDestination(item.destination());
            return entity;
        }
        ResItem resItem = element.getAnnotation(ResItem.class);
        if(resItem != null) {
            entity.setType(ItemType.RES);
            entity.setNameId(resItem.name());
            entity.setDescriptionId(resItem.description());
            entity.setDestination(resItem.destination());
            return entity;
        }
        return null;
    }

    private String getTestGroup(TypeElement typeElement) {
        ItemGroup itemGroup = typeElement.getAnnotation(ItemGroup.class);
        if(itemGroup == null) {
            DefaultValue defaultValue = ItemGroup.class.getAnnotation(DefaultValue.class);
            return defaultValue.value();
        }
        return itemGroup.value();
    }
}