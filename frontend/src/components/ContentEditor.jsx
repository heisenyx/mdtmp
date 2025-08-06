import './ContentEditor.css'

import { useState, useEffect, useCallback } from 'react'
import { EditorContent, useEditor } from '@tiptap/react'
import { StarterKit } from '@syfxlin/tiptap-starter-kit'
import toast from 'react-hot-toast';
import { savePublication, enhancePublication } from '../services/client';
import { Markdown } from 'tiptap-markdown';
import TextAlign from '@tiptap/extension-text-align';

const loadDraft = () => {
    try {
        return JSON.parse(localStorage.getItem('draft') || '{}')
    } catch {
        return {}
    }
}

export default function ContentEditor() {
    const draft = loadDraft()
    const [title, setTitle] = useState(draft.t || '')
    const [author, setAuthor] = useState(draft.a || '')
    const [content, setContent] = useState(draft.c || 'This is a basic example of usage. Press `/` to see available commands. Click on Image to resize and align. ![](https://placehold.co/800x400/6A00F5/white)')

    const validate = useCallback((title, content) => {
        if (!title.trim()) {
            toast.error('Please enter a title before continuing')
            return false
        }
        if (content.trim().length < 20) {
            toast.error('Content is too short!')
            return false
        }
        return true
    }, [])

    const handlePublish = useCallback(async () => {

        const mdContent = editor.storage.markdown.getMarkdown();
        if (!validate(title, mdContent)) return;

        try {
            await toast.promise(
                savePublication(title, author, mdContent, 100),
                {
                    loading: 'Publishing...',
                    success: <b>Successfully published!</b>,
                    error: <b>Something went wrong!</b>,
                }
            )
        } catch (e) {
            console.error(e)
        }
    });

    const handleEnhance = useCallback(async () => {

        const mdContent = editor.storage.markdown.getMarkdown();
        if (!validate(title, mdContent)) return;

        try {
            const response = await toast.promise(
                enhancePublication(title, mdContent),
                {
                    loading: 'Just a moment, please...',
                    success: <b>Enhanced!</b>,
                    error: <b>Something went wrong!</b>,
                }
            )
            const enhancedMarkdown = response.data
            setContent(enhancedMarkdown)
            editor.commands.setContent(enhancedMarkdown)
        } catch (e) {
            console.error(e)
        }
    });

    const editor = useEditor({
        extensions: [
            StarterKit.configure({ slashMenu: true, floatMenu: false }),
            Markdown,
            TextAlign.configure({
                types: ['heading', 'paragraph'],
                defaultAlignment: 'left',
            }),
        ],
        content,
        autofocus: true,
        onUpdate({ editor }) {
            setContent(editor.storage.markdown.getMarkdown())
        }
    })

    useEffect(() => {
        localStorage.setItem(
            'draft',
            JSON.stringify({ t: title, a: author, c: content })
        )
    }, [title, author, content])

    return (
        <>
            <form className="editor-container">
                <input
                    type="text"
                    className="input-field"
                    placeholder="Title"
                    value={title}
                    onChange={e => setTitle(e.target.value)}
                />
                <input
                    type="text"
                    className="input-field author"
                    placeholder="Author"
                    value={author}
                    onChange={e => setAuthor(e.target.value)}
                />

                <EditorContent editor={editor} />
            </form>

            <div className='buttons-container'>
                <button
                    type="button"
                    className='btn btn-secondary'
                    onClick={handleEnhance}
                >
                    Enhance
                </button>
                <button
                    type="button"
                    className='btn btn-primary'
                    onClick={handlePublish}
                >
                    Publish
                </button>
            </div>
        </>
    )
}