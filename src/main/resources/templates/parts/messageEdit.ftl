<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
    Редактор предложений
</a>
<div class="collapse <#if message??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="title" class="form-control ${(titleError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.title}</#if>" name="title" placeholder="Введите заголовок" />
                <#if titleError??>
                    <div class="invalid-feedback">
                        ${titleError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.text}</#if>" name="text" placeholder="Введите краткую информацию про предложения" />
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="full_text" class="form-control ${(full_textError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.full_text}</#if>" name="full_text" placeholder="Введите подробности предложения" />
                <#if full_textError??>
                    <div class="invalid-feedback">
                        ${full_textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="cost" class="form-control ${(costError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.cost}</#if>" name="cost" placeholder="Введите стоимость" />
                <#if costError??>
                    <div class="invalid-feedback">
                        ${costError}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <label>Тэги:</label><br>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" name="tag" id="offline" value="Оффлайн" <#if message?has_content && message.tag == 'offline'>checked</#if>>
                    <label class="form-check-label" for="offline">Оффлайн</label>
                </div>

                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" name="tag" id="online" value="Онлайн" <#if message?has_content && message.tag == 'online'>checked</#if>>
                    <label class="form-check-label" for="online">Онлайн</label>
                </div>

                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" name="tag" id="repetitor" value="Репетитор"
                           <#if message?has_content && message.tag == 'repetitor'>checked</#if>>
                    <label class="form-check-label" for="repetitor">Репетитор</label>
                </div>

                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Выберите файл изображения</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>" />
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Сохранить предложение</button>
            </div>
        </form>
    </div>
</div>
